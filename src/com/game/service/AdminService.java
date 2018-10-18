package com.game.service;

import com.game.util.ConfigData;
import com.game.util.GameData;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Created by lucky on 2018/10/17.
 */
@Service
public class AdminService {
    private static Logger logger = Logger.getLogger(AdminService.class);

    public void reloadConfig() throws Exception {
        logger.warn("reload game config begin...");
        GameData.loadConfigData();
        ConfigData.init();
        logger.warn("reload game config finish...");
    }
}
