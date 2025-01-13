package com.halfgallon.withcon.domain.notification.service.redis.service.impl;

import com.halfgallon.withcon.domain.notification.service.redis.service.RedisStringService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisStringServiceImpl implements RedisStringService {

  private final RedisTemplate<String, String> redisTemplate;

  @Override
  public String get(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  @Override
  public void save(String Key, String value) {
    redisTemplate.opsForValue().set(Key, value);
  }

  @Override
  public void delete(String Key) {
    redisTemplate.delete(Key);
  }
}
