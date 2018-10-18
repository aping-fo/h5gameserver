package com.game.domain.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.util.JsonUtils;
import com.google.common.collect.Lists;

import java.util.List;

public class Player {
    private String openId;
    private String nickName;
    private int level;
    private int exp;

    @JsonIgnore
    private int totalQuestions; //总题数
    @JsonIgnore
    private int answerSuccess; //正确答题数
    @JsonIgnore
    private String historyCatergorysStr = ""; //历史选题类型
    @JsonIgnore
    private List<Integer> historyCatergorys = Lists.newArrayList();
    @JsonIgnore
    private String historyQuestionsStr = ""; //历史答题
    @JsonIgnore
    private List<Integer> historyQuestions = Lists.newArrayList();
    //第几回合
    private int round;
    @JsonIgnore
    public volatile boolean victory = true;

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getHistoryQuestionsStr() {
        return historyQuestionsStr;
    }

    public void setHistoryQuestionsStr(String historyQuestionsStr) {
        this.historyQuestionsStr = historyQuestionsStr;
    }

    public List<Integer> getHistoryQuestions() {
        return historyQuestions;
    }

    public void addHistoryQuestion(int questionId) {
        if (historyQuestions == null)
            return;
        historyQuestions.add(0, questionId);
        if (historyQuestions.size() > 30) {
            historyQuestions.remove(30);
        }
        historyQuestionsStr = JsonUtils.object2String(historyQuestions);
    }

    public void setHistoryQuestions(List<Integer> historyQuestions) {
        this.historyQuestions = historyQuestions;
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

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getAnswerSuccess() {
        return answerSuccess;
    }

    public void setAnswerSuccess(int answerSuccess) {
        this.answerSuccess = answerSuccess;
    }

    public String getHistoryCatergorysStr() {
        return historyCatergorysStr;
    }

    public void setHistoryCatergorysStr(String historyCatergorysStr) {
        this.historyCatergorysStr = historyCatergorysStr;
    }

    public List<Integer> getHistoryCatergorys() {
        return historyCatergorys;
    }

    public void setHistoryCatergorys(List<Integer> historyCatergorys) {
        this.historyCatergorys = historyCatergorys;
    }

    public void addHistoryCatergory(int catergory) {
        historyCatergorys.add(catergory);
        historyCatergorysStr = JsonUtils.object2String(historyCatergorys);
    }
}
