package com.example.springboot.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/cache")
public class CacheDemoController {

    @Autowired
    private RedisTemplate redisTemplate;


    @PostMapping("/put")
    @ResponseBody
    public User put(@RequestBody User user){
        redisTemplate.opsForValue().set(user.getName(), user.getAge());
        return user;
    }
    @PostMapping("/putWithTTL")
    @ResponseBody
    public User putWithTTL(@RequestBody User user){
        redisTemplate.opsForValue().set(user.getName(), user.getAge(), 30, TimeUnit.SECONDS);
        return user;
    }


    @GetMapping("/get")
    @ResponseBody
    public Object get(@RequestParam("key") String key){
        return redisTemplate.opsForValue().get(key);
    }
}
