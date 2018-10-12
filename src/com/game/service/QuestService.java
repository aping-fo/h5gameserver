package com.game.service;

import com.game.domain.quest.Matcher;
import com.game.domain.quest.Room;
import com.game.sdk.net.Result;
import com.game.sdk.proto.StartMatchReq;
import com.game.sdk.utils.ErrorCode;
import com.game.util.TimerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class QuestService extends AbstractService {
    private static Logger logger = Logger.getLogger(QuestService.class);

    @Autowired
    private TimerService timerService;


    /**
     * 匹配房间,玩家ID 对房间
     */
    private final Map<Integer, Room> allRooms = new ConcurrentHashMap<>();
    private final Map<String, Matcher> allMatchers = new ConcurrentHashMap<>();
    private final int MATCH_TIMES = 5;
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
        }, 1, 1, TimeUnit.SECONDS);
    }

    private void doMatching() {
        for (Matcher source : allMatchers.values()) {
            if (source.matchFlag //已经匹配成功
                    || source.exitFlag) { ////玩家退出游戏
                continue;
            }

            for (int i = 0; i < MATCH_TIMES; i++) {
                for (Matcher target : allMatchers.values()) {
                    if (source.getOpenId().equals(target)) { //自己
                        continue;
                    }

                    if (source.matchFlag //已经匹配成功
                            || source.exitFlag) { //玩家退出游戏
                        break;
                    }

                    if (target.exitFlag //玩家退出游戏
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

        allMatchers.remove(matcher1.getOpenId());
        allMatchers.remove(matcher2.getOpenId());

        allRooms.put(roomID, room);
    }

    private void matchingRobot(Matcher source) {

    }

    private void doCheckTimeOut() {

    }


    public Result startMatch(String openId, StartMatchReq req) {
        Matcher matcher = new Matcher(openId, req.getNickName());
        allMatchers.put(openId, matcher);
        return Result.valueOf(ErrorCode.OK, "ok");
    }

    public Result endMatch(String openId) {
        Matcher matcher = allMatchers.remove(openId);
        matcher.exitFlag = true;
        return Result.valueOf(ErrorCode.OK, "ok");
    }

}
