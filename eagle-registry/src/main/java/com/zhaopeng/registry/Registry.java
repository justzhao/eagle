package com.zhaopeng.registry;

import java.util.List;

import com.zhaopeng.common.bean.Url;
import com.zhaopeng.registry.listener.ChildListener;

/**
 * Created by zhaopeng on 2018/7/6.
 */
public interface Registry {



    public void registerUrl(Url url);

    /**
     * 订阅服务并且添加监听器
     * @param url
     * @param childListener
     * @return
     */
    List<String> subscribe(Url url, ChildListener childListener);

}
