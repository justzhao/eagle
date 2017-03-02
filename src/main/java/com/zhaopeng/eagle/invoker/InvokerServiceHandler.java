package com.zhaopeng.eagle.invoker;

import com.zhaopeng.eagle.common.Constants;
import com.zhaopeng.eagle.entity.RPCFuture;
import com.zhaopeng.eagle.entity.Request;
import com.zhaopeng.eagle.entity.Response;
import com.zhaopeng.eagle.entity.URL;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhaopeng on 2016/10/30.
 */
public class InvokerServiceHandler extends SimpleChannelInboundHandler<Response> {

    private final static Logger logger = LoggerFactory.getLogger(InvokerServiceHandler.class);
    ConcurrentHashMap<String, RPCFuture> rpcFutureConcurrentHashMap = new ConcurrentHashMap<>();

    private volatile Channel channel;

    private URL url;

    public InvokerServiceHandler(){
        super();
    }


    public InvokerServiceHandler(URL url){
        super();
        this.url=url;
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, Response response) throws Exception {
        RPCFuture future = rpcFutureConcurrentHashMap.get(response.getRequestId());
        if (future != null) {
            future.setResponse(response);
            rpcFutureConcurrentHashMap.remove(response.getRequestId());
            ctx.close();
        } else {
            logger.error("not response");
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        channel = ctx.channel();
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        super.channelInactive(ctx);
    }


    public RPCFuture sendRequest(Request request) {


        RPCFuture rpcFuture = new RPCFuture(request);
        int timeout=url.getParameter(Constants.TIME_OUT,1000);
        rpcFuture.setTimeout(timeout);
        rpcFutureConcurrentHashMap.put(request.getRequestId(), rpcFuture);
        ChannelFuture future = channel.writeAndFlush(request);
        future.addListener(new ChannelFutureListener() {
            public void operationComplete(final ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                   logger.info(" 成功调用 !");
                }
            }
        });
        return rpcFuture;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        logger.error(" error !");
        cause.printStackTrace();
    }

}
