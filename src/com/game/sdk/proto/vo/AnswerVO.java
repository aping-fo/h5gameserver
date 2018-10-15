package com.game.sdk.proto.vo;

/**
 * Created by lucky on 2018/10/15.
 */
public class AnswerVO {
    private String openid;
    private int cfgid;
    private boolean result;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public int getCfgid() {
        return cfgid;
    }

    public void setCfgid(int cfgid) {
        this.cfgid = cfgid;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
