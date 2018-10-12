package com.game.domain.player;

/**
 * 本对象存储一些业务系统的数据 最终将系列化成json字节，压缩后存入数据库
 */
public class PlayerData {

    private int playerId;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
