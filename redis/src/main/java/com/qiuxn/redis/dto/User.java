package com.qiuxn.redis.dto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.logging.Logger;

public class User implements InitializingBean, DisposableBean {


    @PostConstruct
    public void post(){
        System.out.println("@PostConstruct");
    }

    @PreDestroy
    public void d(){
        System.out.println("@PostConstruct");
    }


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
