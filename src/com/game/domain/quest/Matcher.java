package com.game.domain.quest;

/**
 * Created by lucky on 2018/10/11.
 */
public class Matcher {
    private String openId;
    private String nickName;
    private int roomId;
    private boolean robot = false;

    public volatile boolean matchFlag;
    //上次匹配时间
    public long time;
    //匹配次数
    public int count;
    public int selfMatchCount;
    //取消标记
    public volatile boolean exitFlag;
    public volatile boolean timeoutExitFlag;

    public Matcher(String openId, String nickName) {
        this.openId = openId;
        this.nickName = nickName;
    }

    public boolean isRobot() {
        return robot;
    }

    public void setRobot(boolean robot) {
        this.robot = robot;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void clean(){
        roomId = 0;
        matchFlag = false;
    }
}
