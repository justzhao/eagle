package com.zhaopeng.remote.invoker;

import com.zhaopeng.common.Constants;
import com.zhaopeng.common.bean.Url;

import com.zhaopeng.remote.dispacher.ResponseFuture;
import com.zhaopeng.remote.entity.Request;
import com.zhaopeng.remote.transport.impl.NettyClient;
import com.zhaopeng.remote.transport.impl.TransportServer;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by zhaopeng on 2018/7/22.
 */

@Slf4j
public class Invoker {

    private Url url;

    private final NettyClient client;

    public Invoker(Url url) {
        this.url = url;
        client = TransportServer.connect(url);
    }

    public Object invoker(String interfaceName, String methodName, Object[] args,
                          Class<?>[] parameterTypes) {

        Request req = new Request();
        req.setVersion("2.0.0");
        req.setTwoWay(true);
        req.setClassName(interfaceName);
        req.setMethodName(methodName);
        req.setParameters(args);
        req.setParameterTypes(parameterTypes);

        int retries = url.getParameter(Constants.RETRIES, Constants.DEFAULT_RETRIES);
        try {
            while (retries > 0) {
                ResponseFuture future = client.sendMessage(req);
                if (future == null || future.get() == null) {
                    log.error("time out for {} ms  and try {}",
                        url.getParameter(Constants.TIMEOUT_KEY, Constants.DEFAULT_TIMEOUT), retries);
                    retries--;
                    continue;
                }
                if (future.get() != null) {
                    return future.get();
                }

            }

        } catch (Exception e) {
            log.error("sendMessage error {}", e);
        }

        return null;

    }

}
