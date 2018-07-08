package com.zhaopeng.registry.listener;

import java.util.List;

/**
 * Created by zhaopeng on 2018/7/8.
 */
public interface ChildListener {

    void childChanged(String path, List<String> children);

}
