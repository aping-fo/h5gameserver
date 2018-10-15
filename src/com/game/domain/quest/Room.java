package com.game.domain.quest;

import com.game.util.RandomUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.game.sdk.proto.vo.QuestVO;

import java.util.Map;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by lucky on 2018/10/11.
 */
public class Room {
    private int id;
    private long startTime;
    private Map<String, Matcher> roles = Maps.newConcurrentMap();
    private List<QuestVO> questions = Lists.newArrayList();
    private Map<Integer, Answer> answers = Maps.newHashMap();

    private final ReentrantLock lock = new ReentrantLock();
    private QuestVO currentQuest;
    private List<QuestVO> currentQuestions = Lists.newArrayList();
    private String answerOpendid;
    private String victoryOpenid;

    public String getVictoryOpenid() {
        return victoryOpenid;
    }

    public void setVictoryOpenid(String victoryOpenid) {
        this.victoryOpenid = victoryOpenid;
    }

    public Map<Integer, Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<Integer, Answer> answers) {
        this.answers = answers;
    }

    public String getAnswerOpendid() {
        return answerOpendid;
    }

    public void setAnswerOpendid(String answerOpendid) {
        this.answerOpendid = answerOpendid;
    }

    public QuestVO getCurrentQuest() {
        return currentQuest;
    }

    public void setCurrentQuest(QuestVO currentQuest) {
        this.currentQuest = currentQuest;
    }

    public List<QuestVO> getCurrentQuestions() {
        return currentQuestions;
    }

    public void setCurrentQuestions(List<QuestVO> currentQuestions) {
        this.currentQuestions = currentQuestions;
    }

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

    public QuestVO randomQuest() {
        try {
            lock.lock();
            if (currentQuest == null && currentQuestions.size() > 0) {
                int idx = RandomUtil.randInt(currentQuestions.size());
                currentQuest = currentQuestions.get(idx);
                currentQuestions.remove(idx);
            }
            return currentQuest;

        } finally {
            lock.unlock();
        }
    }

    public String robAnswer(String openid) {
        try {
            lock.lock();
            if (answerOpendid == null) {
                answerOpendid = openid;
            }
            return answerOpendid;
        } finally {
            lock.unlock();
        }
    }

    public void cleanQuest() {
        try {
            lock.lock();
            currentQuest = null;
            answerOpendid = null;
        } finally {
            lock.unlock();
        }
    }
}
