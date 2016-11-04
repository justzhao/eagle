package com.zhaopeng.eagle.invoker;

import com.zhaopeng.eagle.entity.RPCFuture;
import com.zhaopeng.eagle.entity.Request;
import com.zhaopeng.eagle.entity.Respone;
import io.netty.channel.*;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhaopeng on 2016/10/30.
 */
public class InvokerServiceHandler extends SimpleChannelInboundHandler<Respone> {


    ConcurrentHashMap<String, RPCFuture> rpcFutureConcurrentHashMap = new ConcurrentHashMap<>();

    private volatile Channel channel;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Respone respone) throws Exception {

        System.out.println("invoker  read !");

        RPCFuture future = rpcFutureConcurrentHashMap.get(respone.getRequestId());
        if (future != null) {
            future.setRespone(respone);
            rpcFutureConcurrentHashMap.remove(respone.getRequestId());

        } else {

            System.out.println(" not respone !");
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channel = ctx.channel();
        System.out.println("Client active ");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client close ");
        super.channelInactive(ctx);
    }


    public RPCFuture sendRequest(Request request) {


        RPCFuture rpcFuture = new RPCFuture(request);
        rpcFutureConcurrentHashMap.put(request.getRequestId(), rpcFuture);
        ChannelFuture future = channel.writeAndFlush(request);
        future.addListener(new ChannelFutureListener() {
            public void operationComplete(final ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {

                    System.out.println(" Successfully send Message !");

                } else {
                    System.out.println("unSuccessfully send message ");
                }
            }
        });
        return rpcFuture;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        System.out.println("error");
        cause.printStackTrace();
    }

}
