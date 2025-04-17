package shop.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import shop.example.service.RedisService;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisServiceImpl implements RedisService {


    private final RedisTemplate<String,Object> redisTemplate;

    @Override
    public void setString(String key, String value,Long expiration) {
        if(!StringUtils.hasLength(key)){
            return;
        }
        log.info("Saving key '{}' with expiration {} seconds", key, expiration);
        redisTemplate.opsForValue().set(key,value,expiration, TimeUnit.SECONDS);
    }
    @Override
    public String getString(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key))
                .map(String::valueOf)
                .orElse(null);
    }

    @Override
    public void setVersionAccessToken(String key, Long value, Long expiration) {
        redisTemplate.opsForValue().set(key,value,expiration, TimeUnit.SECONDS);
    }

    @Override
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void incrementAccessTokenVersion(String key) {
        if(StringUtils.hasLength(key)){
            redisTemplate.opsForValue().increment(key);
        }
    }
}
