package com.example.springboot.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class RedisTransactionDemoController {

    @Autowired
    private RedisTemplate redisTemplate;


    @PostMapping(value="/add", produces="text/plain")
    @ResponseBody
    public String put(@RequestBody List<String> values){

        //execute a transaction
        List<Object> txResults = (List<Object>) redisTemplate.execute(new SessionCallback<List<Object>>() {
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                int i = 0;
                for(String value : values){
                    operations.opsForValue().set("key"+ i++, value);
                    //if(i==1) throw new PermissionDeniedDataAccessException("aa", new Exception());
                }
                //operations.opsForValue().get("key0");
                // This will contain the results of all operations in the transaction
                return operations.exec();
            }
        });
        System.out.println("Number of items added to set: " + txResults.get(0));
        return "Success!";
    }

}
