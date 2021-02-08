package com.example.springboot.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pipelining")
public class PipeliningDemoController {

    @Autowired
    private StringRedisTemplate redisTemplate;


    @PostMapping(value="/add", produces="text/plain")
    @ResponseBody
    public String put(@RequestBody List<String> values){

        //pop a specified number of items from a queue
        List<Object> results = redisTemplate.executePipelined(
                new RedisCallback<Object>() {
                    public Object doInRedis(RedisConnection connection) throws DataAccessException {
                        StringRedisConnection stringRedisConn = (StringRedisConnection)connection;
                        for(int i=0; i< 10; i++) {
                            stringRedisConn.set("key"+i, String.valueOf(i));
                            //Sif(i==5) throw new RedisSystemException("test", new Exception());
                        }
                        return null;
                    }
                });
        System.out.println(results.get(0));

        return "Success!";
    }

}
