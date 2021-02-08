package com.example.springboot.redis;

import io.lettuce.core.ReadFrom;
import io.lettuce.core.models.role.RedisMasterInstance;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * See RedisAutoConfiguration.
 */
@Configuration
public class SpringRedisConfiguration {


    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(10);
        jedisPoolConfig.setMinIdle(5);
        jedisPoolConfig.setMaxTotal(20);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(2000);
        jedisPoolConfig.setMaxWaitMillis(1000);
        return jedisPoolConfig;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcb = JedisClientConfiguration.builder().usePooling();
        jpcb.poolConfig(jedisPoolConfig);
        JedisClientConfiguration jedisClientConfiguration = jpcb.build();
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration("localhost", 6379);
        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
    }
/*
    @Bean
    public GenericObjectPoolConfig genericObjectPoolConfig() {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(10);
        genericObjectPoolConfig.setMinIdle(5);
        genericObjectPoolConfig.setMaxTotal(20);
        genericObjectPoolConfig.setMinEvictableIdleTimeMillis(2000);
        genericObjectPoolConfig.setMaxWaitMillis(1000);
        return genericObjectPoolConfig;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory(GenericObjectPoolConfig genericObjectPoolConfig) {

        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .poolConfig(genericObjectPoolConfig)
                .build();
        LettuceConnectionFactory factory = new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost", 6379),clientConfig);
        factory.setShareNativeConnection(false);// It's important
        return factory;
    }*/

    /*@Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
                .master("mymaster")
                .sentinel("127.0.0.1", 26379)
                .sentinel("127.0.0.1", 26380)
                .sentinel("127.0.0.1", 26381);
        return new JedisConnectionFactory(sentinelConfig);
    }*/

   /* @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
                .master("mymaster")
                .sentinel("127.0.0.1", 26379)
                .sentinel("127.0.0.1", 26380)
                .sentinel("127.0.0.1", 26381);
        return new LettuceConnectionFactory(sentinelConfig);
    }*/

    /*@Bean
    public  RedisConnectionFactory redisConnectionFactory() {

        List<String> nodes = new ArrayList<>();
        nodes.add("localhost:6379");
        nodes.add("localhost:6380");
        nodes.add("localhost:6381");
        nodes.add("localhost:6382");
        nodes.add("localhost:6383");
        nodes.add("localhost:6384");
        return new JedisConnectionFactory(new RedisClusterConfiguration(nodes));
    }*/

    /*@Bean
    public  RedisConnectionFactory redisConnectionFactory() {

        List<String> nodes = new ArrayList<>();
        nodes.add("localhost:6379");
        nodes.add("localhost:6380");
        nodes.add("localhost:6381");
        nodes.add("localhost:6382");
        nodes.add("localhost:6383");
        nodes.add("localhost:6384");
        return new LettuceConnectionFactory(new RedisClusterConfiguration(nodes));
    }*/

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        RedisSerializer<?> stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        return redisTemplate;
    }
}
