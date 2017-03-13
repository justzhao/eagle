package com.zhaopeng.eagle.common;

import java.util.regex.Pattern;

/**
 * Created by zhaopeng on 2017/2/28.
 */
public class Constants {

    public static final String PROVIDER = "provider";

    public static final String CONSUMER = "consumer";

    public static final String PROVIDER_SIDE = "provider";

    public static final String CONSUMER_SIDE = "consumer";

    public static final String ACCEPTS = "accepts";

    public static final String TIME_OUT = "timeout";

    public static final String THREADS = "threads";

    public static final String RETRIES = "retries";


    public static final Pattern COMMA_SPLIT_PATTERN = Pattern
            .compile("\\s*[,]+\\s*");


}
