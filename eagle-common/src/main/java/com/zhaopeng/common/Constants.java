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

    public static final String PROVIDER_SIDE = "provider";

    public static final String CONSUMER_SIDE = "consumer";

    public static final String THREADS = "threads";

    public static final String RETRIES = "retries";

    public static final int DEFAULT_ACCEPTS = 0;

    public static final String IDLE_TIMEOUT_KEY = "idle.timeout";

    public static final String IO_THREADS_KEY = "iothreads";

    public static final String INTERFACE_KEY = "interface";

    public static final String GROUP_KEY = "group";

    public static final String VERSION_KEY = "version";

    public static final String PATH_KEY = "path";

    public static final int DEFAULT_IO_THREADS = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32);

    public static final String TIMEOUT_KEY = "timeout";

    public static final int DEFAULT_TIMEOUT = 1000;

    public static final int DEFAULT_RETRIES = 3;

    public static final int DEFAULT_THREADS = 200;

    public static final int DEFAULT_IDLE_TIMEOUT = 600 * 1000;

    public static final int THREAD_POOL_CORE_SIZE = 10;

    public static final int THREAD_POOL_ALIVE_TIME = 60;

    public static final int THREAD_POOL_QUEUE_SIZE = 1000;



}
