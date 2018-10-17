package com.game.sdk.proto;

import com.game.sdk.proto.vo.PlayerRankVO;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by lucky on 2018/10/16.
 */
public class RankResp {
    private int selfRank;
    private List<PlayerRankVO> rankVOList = Lists.newArrayList();

    public int getSelfRank() {
        return selfRank;
    }

    public void setSelfRank(int selfRank) {
        this.selfRank = selfRank;
    }

    public List<PlayerRankVO> getRankVOList() {
        return rankVOList;
    }

    public void setRankVOList(List<PlayerRankVO> rankVOList) {
        this.rankVOList = rankVOList;
    }
}
