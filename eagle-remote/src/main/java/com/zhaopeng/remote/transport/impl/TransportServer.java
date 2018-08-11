package com.zhaopeng.remote.transport.impl;

import com.zhaopeng.common.bean.Url;
import com.zhaopeng.common.exception.RemotingException;
import com.zhaopeng.remote.transport.Server;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by zhaopeng on 2018/7/16.
 */
@Slf4j
public class TransportServer {

    public  static  Server bind(Url url) {

        try {
            new NettyServer(url);
        } catch (RemotingException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static NettyClient connect(Url url) {

        try {
            return new NettyClient(url);
        }catch (Exception e){

            log.error("connect url {} error {}",url,e);
        }
        return null;

    }
}
