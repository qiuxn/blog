# Spring  bean生命周期



bean的生命周期，即从bean的创建，到bean的销毁，bean经历了什么？

## 1.bean实例化入口

```java
AbstractBeanFactory.getBean(String name)
```

## 2.普通bean实例化

spring.xml定义了User,User实现了InitializingBean、DisposableBean接口

```xml
<bean id="user" class="com.qiuxn.redis.dto.User" init-method="init" destroy-method="clear" lazy-init="false">
    <property name="name" value="张三"/>
    <property name="age" value="18"/>
</bean>
```

```java
package com.qiuxn.redis.dto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.logging.Logger;

public class User implements InitializingBean, DisposableBean {

    public void init(){
        System.out.println("init-method方法执行");
    }
    public void clear(){
        System.out.println("destroy-method方法执行");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("DisposableBean-->destroy");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("InitializingBean-->afterPropertiesSet");
    }

    private String name;
    private int age;
    private String email;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", id=" + id +
                '}';
    }
}
```



同时定义了一个Bean后置处理器

```java
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
```

启动spring工程，输出如下：

-------------------------------------------------

前置处理：张三

InitializingBean-->afterPropertiesSet

init-method方法执行

后置处理：张三后置处理

-----------------------

停止spring工程时，输出如下

DisposableBean-->destroy

destroy-method方法执行

现在结合源码去看AbstractAutowireCapableBeanFactory.doCreateBean

```java
protected Object doCreateBean(String beanName, RootBeanDefinition mbd, @Nullable Object[] args) throws BeanCreationException {
    //.................


    if (instanceWrapper == null) {
        instanceWrapper = this.createBeanInstance(beanName, mbd, args);
    }

    Object bean = instanceWrapper.getWrappedInstance();
	//.................


    Object exposedObject = bean;

    try {
        this.populateBean(beanName, mbd, instanceWrapper);
        exposedObject = this.initializeBean(beanName, exposedObject, mbd);
    } catch (Throwable var18) {
        if (var18 instanceof BeanCreationException && beanName.equals(((BeanCreationException)var18).getBeanName())) {
            throw (BeanCreationException)var18;
        }

        throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Initialization of bean failed", var18);
    }

    try {
        this.registerDisposableBeanIfNecessary(beanName, bean, mbd);
        return exposedObject;
    } catch (BeanDefinitionValidationException var16) {
        throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Invalid destruction signature", var16);
    }
}
```

```java
protected Object initializeBean(String beanName, Object bean, @Nullable RootBeanDefinition mbd) {
    if (System.getSecurityManager() != null) {
        AccessController.doPrivileged(() -> {
            this.invokeAwareMethods(beanName, bean);
            return null;
        }, this.getAccessControlContext());
    } else {
        this.invokeAwareMethods(beanName, bean);
    }

    Object wrappedBean = bean;
    if (mbd == null || !mbd.isSynthetic()) {
        wrappedBean = this.applyBeanPostProcessorsBeforeInitialization(bean, beanName);
    }

    try {
        this.invokeInitMethods(beanName, wrappedBean, mbd);
    } catch (Throwable var6) {
        throw new BeanCreationException(mbd != null ? mbd.getResourceDescription() : null, beanName, "Invocation of init method failed", var6);
    }

    if (mbd == null || !mbd.isSynthetic()) {
        wrappedBean = this.applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
    }

    return wrappedBean;
}
```


![2021-11-06_195553](https://user-images.githubusercontent.com/29935469/140608712-59f545d7-f1ae-4379-840f-7c8025b8c2e6.png)


## 3. @PostConstruct和@PreDestroy

有的人认为它也是生命周期的一部分，其实这个的本质也是利用bean处理器的前置，具体为CommonAnnotationBeanPostProcessor（InitDestroyAnnotationBeanPostProcessor），当bean实例化时，会遍历执行bean处理器的前置方法，会判断bean中的方法有没有被@PostConstruct修饰，有则反射调用；

@PreDestroy同理也是一样，在容器发布关闭通知时，会调用destroy方法，此时也会调用遍历bean处理器CommonAnnotationBeanPostProcessor（DestructionAwareBeanPostProcessor），判断方法是否有@PreDestroy，有则反射调用
