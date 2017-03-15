package com.zhaopeng.eagle.annotation;

import java.lang.annotation.*;

/**
 * Created by zhaopeng on 2017/3/13.
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Service {

    Class<?> interfaceClass() default void.class;

    String interfaceName() default "";

    String version() default "";

    String group() default "";

    boolean export() default false;
}
