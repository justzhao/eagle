package com.zhaopeng.eagle.provider;


import com.zhaopeng.eagle.provider.config.ProviderBootStrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by zhaopeng on 2016/10/30.
 */
public class ProviderConfig {




    public  static  void  main(String args[]) throws Exception {

        ProviderBootStrap.init();

    }
}
