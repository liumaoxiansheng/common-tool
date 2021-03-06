package com.example.commontool.utils.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @ClassName: GetBeanUtil
 * @Description: 获取spring管理的单例bean工具
 * @Author: th_legend
 **/
@Component
public class GetBeanUtil implements ApplicationContextAware, BeanFactoryAware {
    private static ApplicationContext applicationContext = null;

    private static ConfigurableListableBeanFactory beanFactory=null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (GetBeanUtil.applicationContext == null) {
            GetBeanUtil.applicationContext = applicationContext;
        }
    }

    /**
     * 返回ApplicationContext
     *
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 返回bean
     *
     * @param beanName beanName
     * @return bean
     */
    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    /**
     * 获取bean
     *
     * @param c c
     * @param <T> 泛型
     * @return bean
     */
    public static <T> T getBean(Class<T> c) {
        return applicationContext.getBean(c);
    }

    /**
     * 获取bean
     * @param c c
     * @param  name 名称
     * @param <T> 泛型
     * @return T 泛型
     */
    public static <T> T getBean(String name, Class<T> c) {
        return applicationContext.getBean(name, c);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
            throw new IllegalArgumentException(
                    "AutowiredAnnotationBeanPostProcessor requires a ConfigurableListableBeanFactory: " + beanFactory);
        }
        System.out.println("TestBeanFactory.setBeanFactory()");
        if (GetBeanUtil.beanFactory==null) {
            GetBeanUtil.beanFactory= (ConfigurableListableBeanFactory) beanFactory;
        }
    }

    public static void registerBean(String name,Object object){
        beanFactory.registerSingleton(name,object);

    }

    public static void registerTask(String name,Object object){
        ScheduledAnnotationBeanPostProcessor scheduledProcessor = applicationContext.getBean(ScheduledAnnotationBeanPostProcessor.class);
        scheduledProcessor.postProcessAfterInitialization(object,name);
    }

}
