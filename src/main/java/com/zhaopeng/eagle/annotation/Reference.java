package com.zhaopeng.eagle.annotation;

import java.lang.annotation.*;

/**
 * Created by zhaopeng on 2017/3/13.
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Reference {

    int  timeout() default 5000;

    int retries() default 3;

    String interfaceName() default "";

    String id() default "";


}
