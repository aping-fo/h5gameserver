package com.game.sdk.proto.vo;

/**
 * Created by lucky on 2018/10/16.
 */
public class PlayerRankVO {
    private String openid;
    private String nickName;
    private int level;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
