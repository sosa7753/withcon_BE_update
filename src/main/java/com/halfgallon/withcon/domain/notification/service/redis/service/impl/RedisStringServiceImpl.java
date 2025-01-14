package com.halfgallon.withcon.domain.notification.service.redis.service.impl;

import com.halfgallon.withcon.domain.notification.service.redis.service.RedisStringService;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisStringServiceImpl implements RedisStringService {

  private final RedisTemplate<String, String> redisTemplate;

  @Value("${redis_shared_memory_ttl}")
  private Duration ttl;

  @Override
  public String get(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  @Override
  public void save(String key, String value) {
    redisTemplate.opsForValue().set(key, value);
    log.info("공유 메모리 key : {}, value : {}", key, value);
    redisTemplate.expire(key, ttl);
    log.info("공유 메모리 key : {}, ttl : {}", key, ttl);
  }

  @Override
  public void delete(String key) {
    redisTemplate.delete(key);
    log.info("공유 메모리 key 삭제 {}", key);
  }
}
