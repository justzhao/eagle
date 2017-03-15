package com.zhaopeng.eagle.annotation;

import com.zhaopeng.eagle.common.Constants;
import com.zhaopeng.eagle.spring.EagleServiceBean;
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
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * Created by zhaopeng on 2017/3/13.
 * <p>
 * BeanFactoryPostProcessor是在spring容器加载了bean的定义文件之后，在bean实例化之前执行的
 * ApplicationContextAware 属于属性注入，实例化时候会调用
 * BeanPostProcessor，可以在spring容器实例化bean之后，在执行bean的初始化方法前后，添加一些自己的处理逻辑。这里说的初始化方法，指的是下面两种：
 * 1）bean实现了InitializingBean接口，对应的方法为afterPropertiesSet
 * 2）在bean定义的时候，通过init-method设置的方法
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
            // 用来浏览类路径
            ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner((BeanDefinitionRegistry) beanFactory);

            // 指定类型
            AnnotationTypeFilter filter = new AnnotationTypeFilter(Service.class);

            // 浏览需要包括制定的注解类型
            scanner.addIncludeFilter(filter);

            String[] packages = Constants.COMMA_SPLIT_PATTERN.split(annotationPackage);
            // 浏览指定的包路径
            scanner.scan(packages);

        }

    }

    /**
     * 执行bean的初始化方法前，引用服务
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * 执行bean的初始化方法后 ,暴露服务
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (!isMatchPackage(bean)) {
            return bean;
        }
        // 在此处暴露服务的。
        Service service = bean.getClass().getAnnotation(Service.class);

        if (service != null) {

            EagleServiceBean serviceBean = new EagleServiceBean();
            if (void.class.equals(service.interfaceClass())
                    && "".equals(service.interfaceName())) {
                if (bean.getClass().getInterfaces().length <= 0) {
                    throw new IllegalStateException("Failed to export remote service class " + bean.getClass().getName() + ", cause: The @Service undefined interfaceClass or interfaceName, and the service class unimplemented any interfaces.");
                }
                serviceBean.setInterfaceName(bean.getClass().getInterfaces()[0].getName());
            }

            serviceBean.setApplicationContext(applicationContext);

            serviceBean.checkConfig();

            serviceBean.export();
            serviceBean.putRef(bean);

        }

        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.applicationContext = applicationContext;
    }

    /**
     * bean 是否在对应的类路径下面
     *
     * @param bean
     * @return
     */
    private boolean isMatchPackage(Object bean) {
        if (annotationPackages == null || annotationPackages.length == 0) {
            return true;
        }
        String beanClassName = bean.getClass().getName();
        for (String pkg : annotationPackages) {
            if (beanClassName.startsWith(pkg)) {
                return true;
            }
        }
        return false;
    }
}
