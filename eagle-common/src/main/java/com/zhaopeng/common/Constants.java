package com.zhaopeng.common;

import java.util.regex.Pattern;

/**
 * Created by zhaopeng on 2018/7/10.
 */
public class Constants {

    public static final Pattern COMMA_SPLIT_PATTERN = Pattern
            .compile("\\s*[,]+\\s*");


    public static final String DEFAULT_KEY_PREFIX = "default.";


    public static final String ACCEPTS_KEY = "accepts";


    public static final int DEFAULT_ACCEPTS = 0;


    public static final String IDLE_TIMEOUT_KEY = "idle.timeout";

    public static final String IO_THREADS_KEY = "iothreads";

    public static final int DEFAULT_IO_THREADS = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32);




    public static final int DEFAULT_IDLE_TIMEOUT = 600 * 1000;


    public static final int THREAD_POOL_CORE_SIZE = 10;

    public static final int THREAD_POOL_ALIVE_TIME = 60;

    public static final int THREAD_POOL_QUEUE_SIZE=1000;

}
