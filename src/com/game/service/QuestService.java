package com.game.service;

import com.game.domain.player.Player;
import com.game.domain.quest.Matcher;
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
    private final Map<String, Matcher> allMatchers = new ConcurrentHashMap<>();
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
        for (Matcher source : allMatchers.values()) {
            if (source.matchFlag //已经匹配成功
                    || source.isRobot()  //机器人不参与
                    || source.exitFlag) { ////玩家退出游戏
                continue;
            }

            for (int i = 0; i < MATCH_TIMES; i++) {
                for (Matcher target : allMatchers.values()) {
                    if (source.getOpenId().equals(target.getOpenId())) { //自己
                        continue;
                    }

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

            if (source.time >= MATCH_TIMES) {
                matchingRobot(source);
            }
            source.time += 1;
        }
    }

    private void matchSuccess(Matcher matcher1, Matcher matcher2) {
        if (matcher1.exitFlag || matcher2.exitFlag) {
            return;
        }

        matcher1.matchFlag = true;
        matcher2.matchFlag = true;

        int roomID = ROOMID_GEN.getAndIncrement();

        Room room = new Room(roomID);
        room.getRoles().put(matcher1.getOpenId(), matcher1);
        room.getRoles().put(matcher2.getOpenId(), matcher2);

        matcher1.setRoomId(roomID);
        matcher2.setRoomId(roomID);

        allRooms.put(roomID, room);
    }

    /**
     * 匹配机器人
     *
     * @param source
     */
    private void matchingRobot(Matcher source) {
        if (source.exitFlag) {
            return;
        }
        source.matchFlag = true;

        int roomID = ROOMID_GEN.getAndIncrement();
        Room room = new Room(roomID);
        room.getRoles().put(source.getOpenId(), source);

        Matcher robot = new Matcher(UUID.randomUUID().toString(), "ABC");
        robot.setRobot(true);
        robot.matchFlag = true;
        robot.setRoomId(roomID);
        allMatchers.put(robot.getOpenId(), robot);

        room.getRoles().put(robot.getOpenId(), robot);
        allRooms.put(roomID, room);
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
        Matcher matcher = new Matcher(openId, req.getNickName());
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
        Matcher matcher = allMatchers.remove(openId);
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

        Matcher matcher = allMatchers.get(openId);
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
                for (Matcher matcher1 : room.getRoles().values()) {
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

    public Matcher getMatcher(String openId) {
        return allMatchers.get(openId);
    }

    public Room getRoom(int roomId) {
        return allRooms.get(roomId);
    }

    public Room removeRoom(int roomId) {
        Room room = allRooms.remove(roomId);
        for (String openid : room.getRoles().keySet()) {
            allMatchers.remove(openid);
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
        Matcher matcher = new Matcher(player.getOpenId(), player.getNickName());
        matcher.setRoomId(room.getId());
        allMatchers.put(openId, matcher);
        room.getRoles().put(openId, matcher);
        allRooms.put(room.getId(), room);
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
        Matcher matcher = new Matcher(player.getOpenId(), player.getNickName());
        matcher.setRoomId(room.getId());
        allMatchers.put(openId, matcher);
        room.getRoles().put(openId, matcher);
        return Result.valueOf(ErrorCode.OK, String.valueOf(room.getId()));
    }
}
