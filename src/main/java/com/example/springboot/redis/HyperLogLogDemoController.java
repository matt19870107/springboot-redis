package com.example.springboot.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hyperloglog")
public class HyperLogLogDemoController {

    @Autowired
    private RedisTemplate redisTemplate;


    @PostMapping(value="/add", produces="text/plain")
    @ResponseBody
    public String put(@RequestBody List<String> ips){
        ips.stream().forEach(ip -> redisTemplate.opsForHyperLogLog().add("visit_ips", ip));
        return "Success!";
    }

    @GetMapping("/get")
    @ResponseBody
    public Long get(){
        return redisTemplate.opsForHyperLogLog().size("visit_ips");
    }
}
