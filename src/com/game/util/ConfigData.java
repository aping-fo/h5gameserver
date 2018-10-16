package com.game.util;


import com.game.data.GlobalConfig;

import java.util.Collection;

/**
 * 一些不是唯一key的配置，可以在这里定义一些辅助函数操作
 */
public class ConfigData {


    public static GlobalConfig globalCfg;


    public static void init() {
        globalCfg = GameData.getConfig(GlobalConfig.class, 1);
    }


    public static <T> T getConfig(Class<T> t, int id) {
        T cfg = GameData.getConfig(t, id);
        return cfg;
    }

    public static Collection<Object> getConfigs(Class<?> t) {
        return GameData.getConfigs(t);
    }
}
