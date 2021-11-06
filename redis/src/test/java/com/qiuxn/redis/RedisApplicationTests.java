package com.qiuxn.redis;

import com.qiuxn.redis.dto.User;
import com.qiuxn.redis.spring.bean.AppContext;
import com.qiuxn.redis.spring.bean.Car;
import com.qiuxn.redis.spring.bean.CarFactoryBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class RedisApplicationTests {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    AppContext appContext;

    @Autowired
    Car mycar;

    @Test
    void testBeanLifeCycle() throws Exception{
        CarFactoryBean car = applicationContext.getBean(CarFactoryBean.class);
        System.out.println(car.getObject());

        //((AbstractApplicationContext) applicationContext).close();
    }

    @Test
    void contextLoads() {
        System.out.println(mycar);
    }

    @Test
    public void testString() throws Exception{
        Map<String,String> map = new HashMap<>();
        map.put("k1","v1");
        map.put("k2","v2");
        map.put("k3","v3");
        Set<String> keySet = map.keySet();
        stringRedisTemplate.opsForValue().multiSet(map);
        List<String> valueList =  stringRedisTemplate.opsForValue().multiGet(keySet);
        System.out.println(valueList.toString());

        stringRedisTemplate.opsForValue().set("k1","v11",10, TimeUnit.SECONDS);
        System.out.println(stringRedisTemplate.opsForValue().get("k1"));
        stringRedisTemplate.getExpire("k1");
        System.out.println( stringRedisTemplate.getExpire("k1"));
        Thread.sleep(10000);
        System.out.println( stringRedisTemplate.getExpire("k1"));
        System.out.println( stringRedisTemplate.getExpire("k2"));

        System.out.println(stringRedisTemplate.opsForValue().get("k1"));


    }

    @Test
    public void testList(){
        //redisTemplate.opsForList().leftPush("language","java");
        //redisTemplate.opsForList().leftPushAll("language", Arrays.asList("python","C++"));
        //redisTemplate.opsForList().leftPushAll("language", "redis","C");

        ;
        long len = redisTemplate.opsForList().size("language");
        System.out.println();
        List<String> list =  redisTemplate.opsForList().range("language",0,-1);
        System.out.println(list);

        System.out.println(redisTemplate.opsForList().index("language",1));
    }

}
