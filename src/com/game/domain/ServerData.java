package com.game.domain;

import com.game.domain.quest.Fighter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucky on 2018/10/17.
 */
public class ServerData {
    private List<Fighter> masterMatchers = new ArrayList<>();

    public List<Fighter> getMasterMatchers() {
        return masterMatchers;
    }

    public void setMasterMatchers(List<Fighter> masterMatchers) {
        this.masterMatchers = masterMatchers;
    }
}
