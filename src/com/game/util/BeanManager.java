package com.game.util;

import com.game.event.InitHandler;
import com.game.service.AbstractService;
import com.game.service.QuestService;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 管理同类型的bean，登陆，下线，初始化触发等
 *
 * @author luojian
 */
public class BeanManager {

    private static List<AbstractService> initHandlers = new ArrayList<>();

    private static ApplicationContext actx;

    public static ApplicationContext getApplicationCxt() {
        return actx;
    }

    public static <T> T getBean(Class<T> clazz) {
        return actx.getBean(clazz);
    }

    public static <T> T getBean(String clazz) {
        return (T) actx.getBean(clazz);
    }

    public static void handleInit() {
    }

    public static void onStart(ApplicationContext ctx) {
        actx = ctx;
        // 获得有子类
        Map<String, AbstractService> initBeans = ctx.getBeansOfType(AbstractService.class);
        if (initBeans != null) {
            // 将所有的子类添加到集合
            initHandlers.addAll(initBeans.values());
        }

        for (AbstractService handler : initHandlers) {
            handler.onStart();
        }
    }
}
