package com.game.domain.quest;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by lucky on 2018/10/11.
 */
public class Room {
    private int id;
    private Map<String, Matcher> roles = Maps.newConcurrentMap();

    public Room(int id) {
        this.id = id;
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
