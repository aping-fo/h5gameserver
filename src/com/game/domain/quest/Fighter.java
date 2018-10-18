package com.game.domain.quest;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by lucky on 2018/10/11.
 */
public class Fighter {
    private String openId;
    private String nickName;
    private int roomId;
    private boolean robot = false;

    private int level;
    private int exp;
    //大师赛
    private int round = 0; //第几回合

    @JsonIgnore
    public volatile boolean matchFlag; //匹配标识
    //匹配次数
    @JsonIgnore
    public int count; //匹配次数
    //取消标记
    @JsonIgnore
    public volatile boolean exitFlag; //退出标志
    @JsonIgnore
    public volatile boolean victory = false; //是否胜利
    @JsonIgnore
    public boolean fighting = false; //是否答题中

    public Fighter(String openId, String nickName) {
        this.openId = openId;
        this.nickName = nickName;
    }

    public Fighter() {
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
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

    public void clean() {
        roomId = 0;
        matchFlag = false;
        fighting = false;
    }

    public void matchSuccess() {
        matchFlag = true;
        fighting = true;
    }
}
