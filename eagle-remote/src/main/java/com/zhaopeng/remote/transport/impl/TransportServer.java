package com.zhaopeng.remote.transport.impl;

import com.zhaopeng.common.bean.Url;
import com.zhaopeng.common.exception.RemotingException;
import com.zhaopeng.remote.hanlder.ChannelHandler;
import com.zhaopeng.remote.transport.Server;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by zhaopeng on 2018/7/16.
 */
@Slf4j
public class TransportServer {

    public static Server bind(Url url, ChannelHandler handler) {

        try {
            new NettyServer(url, handler);
        } catch (RemotingException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static NettyClient connect(Url url, ChannelHandler handler) {

        try {
            return new NettyClient(url, handler);
        } catch (Exception e) {

            log.error("connect url {} error {}", url, e);
        }
        return null;

    }
}
