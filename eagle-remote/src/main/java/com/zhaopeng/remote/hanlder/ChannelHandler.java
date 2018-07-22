package com.zhaopeng.remote.hanlder;

import com.zhaopeng.common.exception.RemotingException;
import com.zhaopeng.remote.dispacher.DefaultFuture;
import com.zhaopeng.remote.entity.Request;
import com.zhaopeng.remote.entity.Response;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by zhaopeng on 2018/7/15.
 */
public class ChannelHandler {


    Logger logger = LoggerFactory.getLogger(getClass());

    public void connected(Channel channel) throws RemotingException {
    }

    public void disconnected(Channel channel) throws RemotingException {
    }


    public void sent(Channel channel, Object message) throws RemotingException {

        if (message instanceof Request) {
            new DefaultFuture(channel, (Request) message, 0);
            channel.writeAndFlush(message);

        } else {
            channel.writeAndFlush(message);
            logger.error("not request {}", message);
        }

    }

    public void received(Channel channel, Object message) throws RemotingException {


        if (message instanceof Request) {
            // handle request.
            Request request = (Request) message;
            if (request.isHeartEvent()) {
                Response response = new Response(request.getRequestId());
                channel.writeAndFlush(response);
            } else {
                if (request.isTwoWay()) {
                    Response response = new Response(request.getRequestId());
                    try {
                        response.setResult(handle(request));
                    } catch (Throwable throwable) {

                    } finally {
                        channel.writeAndFlush(response);
                    }
                }
            }
        } else if (message instanceof Response) {
            handleResponse(channel, (Response) message);
        }
    }

    public void caught(Channel channel, Throwable ex) {

    }

    private Object handle(Request request) throws InvocationTargetException {
/*        String className = request.getClassName();
        Object serviceBean = ServiceHolder.getService(className);
        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        FastClass serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
        return serviceFastMethod.invoke(serviceBean, parameters);*/

        return null;
    }

    public void handleResponse(Channel channel, Response response) {

    }


}
