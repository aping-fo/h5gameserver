package com.game.sdk.proto;

import com.game.sdk.proto.vo.QuestVO;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by lucky on 2018/10/12.
 */
public class QuestBankResp {
    /**
     * 房间成员
     */
    private List<QuestVO> roles = Lists.newArrayList();

    public List<QuestVO> getRoles() {
        return roles;
    }

    public void setRoles(List<QuestVO> roles) {
        this.roles = roles;
    }
}
