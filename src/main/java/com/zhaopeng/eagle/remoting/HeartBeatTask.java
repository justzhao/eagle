package com.zhaopeng.eagle.remoting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhaopeng on 2017/3/9.
 */
public class HeartBeatTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(HeartBeatTask.class);

    public HeartBeatTask(HeartBeatAction action) {
        this.action = action;
    }

    HeartBeatAction action;

    @Override
    public void run() {
        action.runHearBeat();
    }
}
