package com.example.url_shortener.cache;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    private final StringRedisTemplate redisTemplate;

    public CacheService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void set(String key, String value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    public String get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key){
        redisTemplate.delete(key);
    }

    public boolean exists(String key){
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

}
