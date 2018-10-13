package com.game.domain.player;

import com.google.common.collect.Lists;

import java.util.List;

public class Player {
    private String openId;
    private String nickName;
    private int level;
    private int totalQuestions; //总题数
    private int answerSuccess; //正确答题数
    private String historyCatergorysStr = ""; //历史选题类型
    private List<Integer> historyCatergorys = Lists.newArrayList();
    private String historyQuestionsStr = ""; //历史答题
    private List<Integer> historyQuestions = Lists.newArrayList();

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
}
