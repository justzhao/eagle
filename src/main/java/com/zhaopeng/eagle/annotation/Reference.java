package com.zhaopeng.eagle.annotation;

import java.lang.annotation.*;

/**
 * Created by zhaopeng on 2017/3/13.
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Reference {


}
