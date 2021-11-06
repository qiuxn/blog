package com.qiuxn.redis.spring.bean;

import com.qiuxn.redis.dto.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof User){
            User user = (User)bean;
            System.out.println("前置处理："+user.getName());
        }
        if(bean instanceof Car){
            System.out.println("前置处理："+((Car) bean).getBrand());
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof User){
            User user = (User)bean;
            user.setName(user.getName()+"后置处理");
            System.out.println("后置处理："+user.getName());
        }
        return bean;
    }
}
