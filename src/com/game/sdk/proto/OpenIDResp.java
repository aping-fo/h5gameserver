package com.game.sdk.proto;

/**
 * Created by lucky on 2018/10/11.
 */
public class OpenIDResp {
    private String openId;
    private boolean hasRole;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public boolean isHasRole() {
        return hasRole;
    }

    public void setHasRole(boolean hasRole) {
        this.hasRole = hasRole;
    }
}
