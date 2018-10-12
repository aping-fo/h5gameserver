package com.game.sdk.utils;

import com.game.sdk.annotation.Command;
import com.game.sdk.annotation.Handler;
import com.game.sdk.net.Exector;
import com.game.util.BeanManager;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lucky on 2018/10/11.
 */
public class ExectorManager {
    private final static Map<Integer, Exector> commands = new HashMap<>();


    public static Exector getExector(int cmd) {
        return commands.get(cmd);
    }

    public static void loadExec() {

        Map<String, Object> handlerMap = BeanManager.getApplicationCxt().getBeansWithAnnotation(Handler.class);

        for (Object instance : handlerMap.values()) {
            Class<?> clazz = instance.getClass();
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                Command methodAnno = method.getAnnotation(Command.class);
                if (methodAnno != null) {
                    int cmd = methodAnno.cmd();
                    if (commands.containsKey(cmd)) {
                        throw new RuntimeException("Found Same Extension Method:" + cmd);
                    }
                    commands.put(cmd, new Exector(instance, method));
                }
            }
        }
    }
}
