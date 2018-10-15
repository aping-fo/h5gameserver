package com.game.sdk.proto;

import com.game.sdk.proto.vo.AnswerVO;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by lucky on 2018/10/12.
 */
public class AnswerResultResp {

    private List<AnswerVO> answerVOS = Lists.newArrayList();

    public List<AnswerVO> getAnswerVOS() {
        return answerVOS;
    }

    public void setAnswerVOS(List<AnswerVO> answerVOS) {
        this.answerVOS = answerVOS;
    }
}
