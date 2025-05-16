package cn.tedu.charging.order.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 该工具类用于获取spring容器中的bean
 */
@Slf4j
@Component
public class SpringContextJobUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     *
     * @param name
     * @param clazz
     * @return
     * @param <T>
     */
    public static <T> T getBean(String name, Class<T> clazz){
        return applicationContext.getBean(name,clazz);
    }

    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

}
