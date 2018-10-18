package com.game.service;

import com.game.domain.player.Player;
import com.game.domain.quest.Fighter;
import com.game.domain.quest.Room;
import com.game.sdk.net.Result;
import com.game.sdk.proto.HistoryQuestionResp;
import com.game.sdk.proto.MatchResultResp;
import com.game.sdk.proto.StartMatchReq;
import com.game.sdk.proto.vo.MatcherVO;
import com.game.sdk.utils.ErrorCode;
import com.game.util.TimerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class QuestService extends AbstractService {
    private static Logger logger = Logger.getLogger(QuestService.class);

    @Autowired
    private TimerService timerService;
    @Autowired
    private PlayerService playerService;

    /**
     * 匹配房间,玩家ID 对房间
     */
    private final Map<Integer, Room> allRooms = new ConcurrentHashMap<>();
    private final Map<String, Fighter> allMatchers = new ConcurrentHashMap<>();
    private final int MATCH_TIMES = 2;
    private final AtomicInteger ROOMID_GEN = new AtomicInteger(100); //房间ID

    @Override
    public void onStart() {
        //5S定时
        timerService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    doMatching();
                } catch (Exception e) {
                    logger.error("match schedule error", e);
                }
            }
        }, 10, 5, TimeUnit.SECONDS);

        //1S定时
        timerService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    doCheckTimeOut();
                } catch (Exception e) {
                    logger.error("check timeout schedule error", e);
                }
            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    private void doMatching() {
        for (Fighter source : allMatchers.values()) {
            if (source.matchFlag //已经匹配成功
                    || source.isRobot()  //机器人不参与
                    || source.exitFlag////玩家退出游戏
                    || source.getRoomId() != 0) {
                continue;

            }

            for (int i = 0; i < MATCH_TIMES; i++) {
                for (Fighter target : allMatchers.values()) {
                    if (source.getOpenId().equals(target.getOpenId())) { //自己
                        continue;
                    }
                    if (target.getRoomId() != 0)
                        continue;
                    if (source.matchFlag //已经匹配成功
                            || source.exitFlag) { //玩家退出游戏
                        break;
                    }

                    if (target.exitFlag //玩家退出游戏
                            || target.isRobot()  //机器人不参与
                            || target.matchFlag) { //已经匹配成
                        continue;
                    }

                    matchSuccess(source, target);
                    break;
                }
            }

            if (source.count >= MATCH_TIMES) {
                matchingRobot(source);
            }
            source.count += 1;
        }
    }

    private void matchSuccess(Fighter redFighter, Fighter bludFighter) {
        if (redFighter.exitFlag || bludFighter.exitFlag) {
            return;
        }

        createRoomWithTwo(redFighter, bludFighter);
    }

    /**
     * 匹配机器人
     *
     * @param redFighter
     */
    private void matchingRobot(Fighter redFighter) {
        if (redFighter.exitFlag) {
            return;
        }
        Fighter robot = new Fighter(UUID.randomUUID().toString(), "ABC");
        robot.setRobot(true);

        createRoomWithTwo(redFighter, robot);
    }

    /**
     * 创建红蓝对战房间
     *
     * @param redFighter
     * @param bludFighter
     */
    private Room createRoomWithTwo(Fighter redFighter, Fighter bludFighter) {
        int roomID = ROOMID_GEN.getAndIncrement();
        Room room = new Room(roomID);

        //read Square
        redFighter.matchSuccess();
        redFighter.setRoomId(roomID);
        allMatchers.put(redFighter.getOpenId(), redFighter);
        room.getRoles().put(redFighter.getOpenId(), redFighter);

        //blud Square
        bludFighter.matchSuccess();
        bludFighter.setRoomId(roomID);
        allMatchers.put(bludFighter.getOpenId(), bludFighter);
        room.getRoles().put(bludFighter.getOpenId(), bludFighter);

        allRooms.put(roomID, room);

        return room;
    }

    /**
     * 超时检查
     */
    private void doCheckTimeOut() {
        long now = System.currentTimeMillis();
        for (Room room : allRooms.values()) {
            if (now - room.getStartTime() > TimeUnit.MINUTES.toMillis(3)) {
                //TODO 超时处理


                removeRoom(room.getId());
            }
        }
    }


    /**
     * 请求匹配
     *
     * @param openId
     * @param req
     * @return
     */
    public Result startMatch(String openId, StartMatchReq req) {
        Fighter matcher = new Fighter(openId, req.getNickName());
        allMatchers.put(openId, matcher);
        return Result.valueOf(ErrorCode.OK, "ok");
    }

    /**
     * 退出匹配
     *
     * @param openId
     * @return
     */
    public Result endMatch(String openId) {
        Fighter matcher = allMatchers.remove(openId);
        matcher.exitFlag = true;
        return Result.valueOf(ErrorCode.OK, "ok");
    }

    /**
     * 查询匹配结果
     *
     * @param openId
     * @return
     */
    public Result queryMatchResult(String openId) throws Exception {
        MatchResultResp resp = new MatchResultResp();
        String code = ErrorCode.OK;

        Fighter matcher = allMatchers.get(openId);
        if (matcher == null) {
            return Result.valueOf(ErrorCode.ROLE_NOT_EXIST, resp);
        }

        if (matcher.getRoomId() != 0) {
            Room room = allRooms.get(matcher.getRoomId());
            if (room == null) {
                matcher.clean();
                resp.setMatchSuccess(false);
            } else {
                resp.setMatchSuccess(true);
                for (Fighter matcher1 : room.getRoles().values()) {
                    MatcherVO vo = new MatcherVO();
                    vo.setOpenId(matcher1.getOpenId());
                    vo.setNickName(matcher1.getNickName());
                    vo.setRoomId(matcher1.getRoomId());
                    vo.setRobot(false);

                    resp.getRoles().add(vo);
                }
            }
        } else {
            resp.setMatchSuccess(false);
        }

        return Result.valueOf(code, resp);
    }

    /**
     * 查询历史题库
     *
     * @param openId
     * @return
     */
    public Result getHistoryQuestion(String openId) throws Exception {
        String code = ErrorCode.OK;
        HistoryQuestionResp resp = new HistoryQuestionResp();


        return Result.valueOf(code, resp);
    }

    public Fighter getMatcher(String openId) {
        return allMatchers.get(openId);
    }

    public Room getRoom(int roomId) {
        return allRooms.get(roomId);
    }

    public Room removeRoom(int roomId) {
        Room room = allRooms.remove(roomId);
        for (String openid : room.getRoles().keySet()) {
            Fighter fighter = allMatchers.remove(openid);
            fighter.fighting = false; //答题结束
        }
        return room;
    }

    /**
     * 创建房间
     *
     * @param openId
     * @return
     */
    public Result createRoom(String openId) {
        Player player = playerService.getPlayer(openId);
        Room room = new Room(ROOMID_GEN.getAndDecrement());
        Fighter matcher = new Fighter(player.getOpenId(), player.getNickName());
        matcher.setRoomId(room.getId());
        allMatchers.put(openId, matcher);
        room.getRoles().put(openId, matcher);
        allRooms.put(room.getId(), room);
        return Result.valueOf(ErrorCode.OK, String.valueOf(room.getId()));
    }

    /**
     * 创建房间
     *
     * @return
     */
    public Result createRoom(Fighter redFigher, Fighter bludFighter) {
        Room room = createRoomWithTwo(redFigher, bludFighter);
        return Result.valueOf(ErrorCode.OK, String.valueOf(room.getId()));
    }

    /**
     * 加入房间
     *
     * @param openId
     * @param roomID
     * @return
     */
    public Result joinRoom(String openId, int roomID) {
        Player player = playerService.getPlayer(openId);
        Room room = allRooms.get(roomID);
        if (room == null) {
            return Result.valueOf(ErrorCode.ROOM_NOT_EXIST, "");
        }
        Fighter matcher = new Fighter(player.getOpenId(), player.getNickName());
        matcher.setRoomId(room.getId());
        allMatchers.put(openId, matcher);
        room.getRoles().put(openId, matcher);
        return Result.valueOf(ErrorCode.OK, String.valueOf(room.getId()));
    }

    public void createMasterFight(List<Fighter> players) {
        List<Fighter> list = new ArrayList<>();

        for (Fighter fighter : players) {
            if (fighter.getRound() == 0 || fighter.victory) { //第一局 或者 上一局胜利者 进入下一轮
                list.add(fighter);
            }

            //本轮默认失败
            fighter.victory = false;
            fighter.setRound(fighter.getRound() + 1);
        }

        if (list.size() < 1) {
            return;
        }

        for (int i = 0; i < list.size(); i += 2) {
            Fighter fighter1 = list.get(i);
            Fighter fighter2;
            if (i + 1 >= list.size()) {
                fighter2 = new Fighter(UUID.randomUUID().toString(), "ABC");
                fighter2.setRobot(true);
                fighter2.matchFlag = true;
            } else {
                fighter2 = list.get(i + 1);
            }
            createRoom(fighter1, fighter2);
        }
    }
}
