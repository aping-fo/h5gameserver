package com.game.sdk.proto;

import com.game.sdk.proto.vo.QuestVO;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by lucky on 2018/10/12.
 */
public class QuestBankResp {
    /**
     *  问题列表
     */
    private List<QuestVO> questions = Lists.newArrayList();

    public List<QuestVO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestVO> questions) {
        this.questions = questions;
    }
}
