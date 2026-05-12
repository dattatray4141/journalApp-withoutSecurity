package com.journal.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
//Used for set and get value
public class RedisService {

    @Autowired
    RedisTemplate redisTemplate;

    // get value from redis
    public <T> T get(String city, Class<T> entityClass) {
        try {
            Object object = redisTemplate.opsForValue().get(city);
            if (object == null) {
                log.info("Redis Object is null That means: first time this Key you are putting in cache or ttl happens here.");
                return null;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(object.toString(), entityClass);
        } catch (Exception e) {
            log.error("Error in get city", e);
            return null;
        }
    }

    //Set value in radius
    //ttl -> cache for time , particular object is stored ttl amount of time,after time out values automatically deletes from cache
    public void set(String key, Object object, Long ttl) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonValue = objectMapper.writeValueAsString(object);
            redisTemplate.opsForValue().set(key,jsonValue, ttl, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Error in set {}", key);
        }

    }
}
