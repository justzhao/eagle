package com.zhaopeng.eagle.provider;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetAddress;

/**
 * Created by zhaopeng on 2016/10/29.
 */
public class ProviderServiceHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // 根据传递的参数：类名，方法名，参数，实例化对象执行方法，返回结果。
        // 收到消息直接打印输出
        System.out.println(ctx.channel().remoteAddress() + " Say : " + msg);

        // 返回客户端消息 - 我已经接收到了你的消息
        ctx.writeAndFlush("Received your message! $_");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
       // ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");

        ctx.writeAndFlush( "Welcome to " + InetAddress.getLocalHost().getHostName() + " service! $_");

        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();// 发生异常，关闭链路
    }


}
