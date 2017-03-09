package com.zhaopeng.eagle.remoting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by zhaopeng on 2017/3/9.
 */
public class HeartBeatClient {

    private static final Logger logger = LoggerFactory.getLogger(HeartBeatClient.class);

    private static final ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(2);


}
