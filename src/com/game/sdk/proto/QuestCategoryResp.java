package com.game.sdk.proto;

import com.google.common.collect.Lists;

import java.util.List;

public class QuestCategoryResp {
    /**
     * 题库类型列表
     */
    private List<Integer> questionCategorys = Lists.newArrayList();

    public List<Integer> getQuestionCategorys() {
        return questionCategorys;
    }

    public void setQuestionCategorys(List<Integer> questionCategorys) {
        this.questionCategorys = questionCategorys;
    }
}
