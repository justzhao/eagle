package com.zhaopeng.spring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * Created by zhaopeng on 2018/7/3.
 */
public abstract class AbstractConfig {


    private final static Logger logger = LoggerFactory.getLogger(AbstractConfig.class);


    protected ApplicationContext applicationContext;

/*    protected List<RegistryConfig> registries = new ArrayList<>();

    protected URL url;

    protected Registry registry;*/

    protected String interfaceName;

    protected int port;

    protected String protocol;

    protected String host;

    protected int threads;

    protected int accepts;

    protected int timeout;

    protected int retries;

}
