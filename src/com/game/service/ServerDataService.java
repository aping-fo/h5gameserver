package com.game.service;

import com.game.domain.ServerData;
import com.game.domain.player.Player;
import com.game.domain.quest.Fighter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by lucky on 2018/10/17.
 */
@Service
public class ServerDataService extends AbstractService {
    private ServerData serverData;
    private ReentrantLock signLock = new ReentrantLock();

    @Autowired
    private QuestService questService;

    public ServerData getServerData() {
        return serverData;
    }


    public void addMasterFighter(Player player) {
        try {
            signLock.lock();
            player.setRound(1);
            int idx = 0;
            for (Fighter fighter : serverData.getMasterMatchers()) {
                if (player.getLevel() > fighter.getLevel()) {
                    break;
                }

                if (player.getLevel() == fighter.getLevel() && player.getExp() > fighter.getExp()) {
                    break;
                }

                idx += 1;
            }

            Fighter fighter = new Fighter(player.getOpenId(), player.getNickName());
            fighter.matchFlag = true;
            fighter.setLevel(player.getLevel());
            fighter.setExp(player.getExp());
            serverData.getMasterMatchers().add(idx, fighter);
            if (serverData.getMasterMatchers().size() > 64) {
                serverData.getMasterMatchers().remove(64);
            }
        } finally {
            signLock.unlock();
        }
    }


    @Override
    public void onStart() {
        serverData = new ServerData();
    }
}
