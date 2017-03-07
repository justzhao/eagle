package com.zhaopeng.eagle.registry.zookeeper;

import java.util.List;

/**
 * Created by zhaopeng on 2017/3/7.
 */
public interface ChildListener {

    void childChanged(String path, List<String> children);
}
