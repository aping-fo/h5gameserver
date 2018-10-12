package com.game.sdk.annotation;

import java.lang.annotation.*;

/**
 * Created by lucky on 2018/10/11.
 */
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Handler {
}
