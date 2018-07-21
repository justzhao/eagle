package com.zhaopeng.remote.transport.impl;

import com.zhaopeng.common.bean.Url;
import com.zhaopeng.common.exception.RemotingException;
import com.zhaopeng.remote.transport.Server;

/**
 * Created by zhaopeng on 2018/7/16.
 */
public class TransportServer {

    public Server bind(Url url) {

        try {
            new NettyServer(url);
        } catch (RemotingException e) {
            e.printStackTrace();
        }

        return null;

    }

    public NettyClient connect(Url url) {

        return new NettyClient(url);
    }
}
