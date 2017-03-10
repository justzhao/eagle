package com.zhaopeng.eagle.remoting;

import com.zhaopeng.eagle.entity.Request;
import com.zhaopeng.eagle.invoker.InvokerServiceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhaopeng on 2017/3/9.
 */
public class HeartBeatClient {

    private static final Logger logger = LoggerFactory.getLogger(HeartBeatClient.class);

    private static final ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(2);


    public static void runHeart(final InvokerServiceHandler handler) {

        HeartBeatTask task=new HeartBeatTask(new HeartBeatAction() {
            @Override
            public void runHearBeat() {
                logger.info("发送心跳 {}",handler.toString());
                Request req = new Request();
                req.setHeartEvent(true);
                req.setRequestId(UUID.randomUUID().toString());
                handler.sendRequest(req);
            }
        });
        scheduled.scheduleWithFixedDelay(task, 5, 10, TimeUnit.SECONDS);
   /*     scheduled.submit(new HeartBeatTask(new HeartBeatAction() {
            @Override
            public void runHearBeat() {
                Request req = new Request();
                req.setHeartEvent(true);
                req.setRequestId(UUID.randomUUID().toString());
                handler.sendRequest(req);
            }
        }));*/

    }


}
