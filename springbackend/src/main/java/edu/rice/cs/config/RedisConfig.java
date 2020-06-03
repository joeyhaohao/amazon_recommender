package edu.rice.cs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Configuration
public class RedisConfig {

    @Value(value = "${redis.host}")
    private String host;

    @Value(value = "${redis.port}")
    private int port;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory
                = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(host);
        jedisConnectionFactory.setPort(port);
        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, List<String>> redisTemplate() {
        RedisTemplate<String, List<String>> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

//    @Bean
//    RedisCacheManager cacheManager() {
//        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate());
//        return redisCacheManager;
//    }

}
