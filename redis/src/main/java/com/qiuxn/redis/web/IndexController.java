package com.qiuxn.redis.web;

import com.qiuxn.redis.dto.User;
import org.redisson.Redisson;
import org.redisson.RedissonLock;
import org.redisson.api.RLock;
import org.redisson.client.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
public class IndexController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private Redisson redisson;

    private final String LOCK_KEY = "ticket-001";

    /**
     * @desc
     *    设置过期时间
     *    谁设置谁释放
     * @probleam:  如果业务时间执行超过5s，此时锁超时被删除
     * @return
     */
    @GetMapping("/buyTicket")
    public String buy(){
        String clientId = UUID.randomUUID().toString();
        try{
            //获取锁
            boolean result = stringRedisTemplate.opsForValue().setIfAbsent(LOCK_KEY,clientId,10, TimeUnit.SECONDS);
            //非原子性
            //stringRedisTemplate.opsForValue().set(LOCK_KEY,"qiuxn");
            //stringRedisTemplate.expire(LOCK_KEY,5,TimeUnit.SECONDS);
            if(!result){
                return "error";
            }
            String msg = null;
            Integer count = Integer.parseInt(stringRedisTemplate.opsForValue().get("count"));
            if(count > 0 ){
                count = count - 1;
                stringRedisTemplate.opsForValue().set("count",count + "");
                System.out.println("购票成功,剩余票数："+count);
                msg = "success";
            }else{
                System.out.println("余票不足");
                msg = "failed";
            }
            return msg;
        }finally {
            if(clientId.equals(stringRedisTemplate.opsForValue().get(LOCK_KEY))){
                //释放锁
                stringRedisTemplate.delete(LOCK_KEY);
            }
        }
    }


    @GetMapping("/buyTicket3")
    public String buy3(){
        synchronized (this){
            Integer count = Integer.parseInt(stringRedisTemplate.opsForValue().get("count"));
            if(count > 0 ){
                count = count - 1;
                stringRedisTemplate.opsForValue().set("count",count + "");
                System.out.println("购票成功，剩余" + count);
                return "success";
            }else{
                System.out.println("余票不足");
                return "failed";
            }
        }
    }


    @GetMapping("/buyTicket2")
    public String buy2(){
        RLock lock = redisson.getLock(LOCK_KEY);
        try{
            lock.lock(10,TimeUnit.SECONDS);

            String msg = null;
            Integer count = Integer.parseInt(stringRedisTemplate.opsForValue().get("count"));
            if(count > 0 ){
                count = count - 1;
                stringRedisTemplate.opsForValue().set("count",count + "");
                System.out.println("购票成功,剩余票数："+count);
                msg = "success";
            }else{
                System.out.println("余票不足");
                msg = "failed";
            }
            return msg;
        }finally {
            lock.unlock();
        }
    }

    @PostMapping("/addUser")
    public void addUser(@RequestBody User user){
        redisTemplate.opsForValue().set("user_"+user.getId(),user);
    }
}
