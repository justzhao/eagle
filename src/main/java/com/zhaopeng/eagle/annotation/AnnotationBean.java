package com.zhaopeng.eagle.annotation;

import com.zhaopeng.eagle.common.Constants;
import com.zhaopeng.eagle.spring.config.AbstractConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

/**
 * Created by zhaopeng on 2017/3/13.
 *
 * BeanFactoryPostProcessor是在spring容器加载了bean的定义文件之后，在bean实例化之前执行的
 * ApplicationContextAware 属于属性注入，实例化时候会调用
 * BeanPostProcessor，可以在spring容器实例化bean之后，在执行bean的初始化方法前后，添加一些自己的处理逻辑。这里说的初始化方法，指的是下面两种：
 *  1）bean实现了InitializingBean接口，对应的方法为afterPropertiesSet
 *  2）在bean定义的时候，通过init-method设置的方法
 */
public class AnnotationBean extends AbstractConfig implements DisposableBean, BeanFactoryPostProcessor, BeanPostProcessor, ApplicationContextAware {



    private final static Logger logger = LoggerFactory.getLogger(AnnotationBean.class);


    private String annotationPackage;

    private String[] annotationPackages;

    private ApplicationContext applicationContext;

    public String getAnnotationPackage() {
        return annotationPackage;
    }

    public void setAnnotationPackage(String annotationPackage) {
        this.annotationPackage = annotationPackage;
        this.annotationPackages = (annotationPackage == null || annotationPackage.length() == 0) ? null
                : Constants.COMMA_SPLIT_PATTERN.split(annotationPackage);
    }

    public String[] getAnnotationPackages() {
        return annotationPackages;
    }


    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        if (annotationPackage == null || annotationPackage.length() == 0) {
            return;
        }

        if (beanFactory instanceof BeanDefinitionRegistry) {
            ClassPathBeanDefinitionScanner scanner=new ClassPathBeanDefinitionScanner((BeanDefinitionRegistry)beanFactory);


        }

    }

    /**
     * 执行bean的初始化方法前
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    /**
     * 执行bean的初始化方法后
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.applicationContext = applicationContext;
    }
}
