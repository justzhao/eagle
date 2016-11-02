package com.zhaopeng.eagle.invoker;
import com.zhaopeng.eagle.entity.RPCFuture;
import io.netty.channel.*;
import java.util.concurrent.ConcurrentHashMap;
/**
 * Created by zhaopeng on 2016/10/30.
 */
public class InvokerServiceHandler extends SimpleChannelInboundHandler<String> {


    ConcurrentHashMap<String,RPCFuture> pendingFuture= new ConcurrentHashMap<>();

    private volatile Channel channel;


    RPCFuture future;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        if(future!=null) {

            future.setResult(msg);
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channel=ctx.channel();
        System.out.println("Client active ");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client close ");
        super.channelInactive(ctx);
    }


    public RPCFuture sendRequest(String rq) {


        RPCFuture rpcFuture=new RPCFuture();

        future=rpcFuture;
      ChannelFuture future1 = channel.writeAndFlush(rq);
        future1.addListener(new ChannelFutureListener() {
            public void operationComplete(final ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                }else {
                    System.out.println("unSuccessfully send message " );
                }
            }
        });
        return rpcFuture;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){

        System.out.println("error");
        cause.printStackTrace();
    }

}
