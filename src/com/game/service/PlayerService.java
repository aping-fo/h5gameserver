package com.game.service;

import com.game.dao.PlayerDAO;
import com.game.domain.player.Player;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class PlayerService {
    private static Logger logger = Logger.getLogger(PlayerService.class);

    private PlayerDAO playerDAO;

    private final LoadingCache<String, Player> players = CacheBuilder.newBuilder()
            .expireAfterAccess(600, TimeUnit.SECONDS)
            .maximumSize(3000)
            .build(new CacheLoader<String, Player>() {
                @Override
                public Player load(String openId) throws Exception {
                    logger.info("Cache loaded for " + openId);
                    return playerDAO.queryPlayer(openId);
                }
            });

    public Player getPlayer(String openId) {
        return players.getUnchecked(openId);
    }
}
