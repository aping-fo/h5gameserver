package com.game.sdk.net;

import java.lang.reflect.Method;

/**
 * Created by lucky on 2018/10/11.
 */
public class Exector {
    public final Object owner;
    public final Method method;
    public final Class<?> paramType;

    public Exector(Object owner, Method method) {
        this.owner = owner;
        this.method = method;
        if (method.getParameterCount() > 1) {
            paramType = method.getParameterTypes()[1];
        } else {
            paramType = null;
        }
    }

    public Object invoke(Object... params) throws Throwable {
        return this.method.invoke(owner, params);
    }
}
