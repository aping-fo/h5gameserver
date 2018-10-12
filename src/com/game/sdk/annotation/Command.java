package com.game.sdk.annotation;

import java.lang.annotation.*;

/**
 * Created by lucky on 2018/2/28.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Command {
    public int cmd();

    public String description() default "";
}
