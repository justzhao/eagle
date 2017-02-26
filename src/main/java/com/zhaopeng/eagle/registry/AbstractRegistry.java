package com.zhaopeng.eagle.registry;

import com.zhaopeng.eagle.entity.URL;

import java.io.File;
import java.util.List;

/**
 * 服务的注册和发现
 * Created by zhaopeng on 2017/2/21.
 */
public abstract class AbstractRegistry implements Registry {

    // URL地址分隔符，用于文件缓存中，服务提供者URL分隔
    private static final char URL_SEPARATOR = ' ';

    // URL地址分隔正则表达式，用于解析文件缓存中服务提供者URL列表
    private static final String URL_SPLIT = "\\s+";

    private URL registryUrl;

    // 本地磁盘缓存文件
    private File file;


    public URL getRegistryUrl() {
        return registryUrl;
    }

    public void setRegistryUrl(URL registryUrl) {
        this.registryUrl = registryUrl;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    /**
     * 注册服务
     * @param url
     */
    public void register(URL url){

    }

    /**
     * 订阅服务
     * @param url
     */
    public List<String> subscribe(URL url){

        return null;
    }

}
