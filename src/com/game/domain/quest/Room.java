package com.game.domain.quest;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.game.sdk.proto.vo.QuestVO;
import java.util.Map;
import java.util.List;

/**
 * Created by lucky on 2018/10/11.
 */
public class Room {
    private int id;
    private long startTime;
    private Map<String, Matcher> roles = Maps.newConcurrentMap();
    private List<QuestVO> questions = Lists.newArrayList();

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

    public List<QuestVO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestVO> questions) {
        this.questions = questions;
    }
}
