package com.game.sdk.proto;

import com.game.sdk.proto.vo.HistoryQuestionVO;

import com.google.common.collect.Lists;
import java.util.List;

public class HistoryQuestionResp {
    /**
     * 房间成员
     */
    private List<HistoryQuestionVO> roles = Lists.newArrayListWithCapacity(6);

    public List<HistoryQuestionVO> getRoles() {
        return roles;
    }

    public void setRoles(List<HistoryQuestionVO> roles) {
        this.roles = roles;
    }
}
