package com.game.domain.quest;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by lucky on 2018/10/11.
 */
public class Room {
    private int id;
    private long startTime;
    private Map<String, Matcher> roles = Maps.newConcurrentMap();

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public Room(int id) {
        this.id = id;
        this.startTime = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<String, Matcher> getRoles() {
        return roles;
    }

    public void setRoles(Map<String, Matcher> roles) {
        this.roles = roles;
    }
}
