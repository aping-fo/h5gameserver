package com.game.sdk.proto;

import com.game.sdk.proto.vo.MatcherVO;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by lucky on 2018/10/12.
 */
public class MatchResultResp {
    /**
     * 是否匹配成功
     */
    private boolean matchSuccess;

    /**
     * 房间成员
     */
    private List<MatcherVO> roles = Lists.newArrayListWithCapacity(2);

    public List<MatcherVO> getRoles() {
        return roles;
    }

    public void setRoles(List<MatcherVO> roles) {
        this.roles = roles;
    }

    public boolean isMatchSuccess() {
        return matchSuccess;
    }

    public void setMatchSuccess(boolean matchSuccess) {
        this.matchSuccess = matchSuccess;
    }
}
