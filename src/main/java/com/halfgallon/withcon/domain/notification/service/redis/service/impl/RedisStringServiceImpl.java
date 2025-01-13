package com.halfgallon.withcon.domain.notification.service.redis.service.impl;

import static com.halfgallon.withcon.global.exception.ErrorCode.REDIS_SUBSCRIBE_FAIL;

import com.halfgallon.withcon.domain.notification.service.redis.service.RedisStringService;
import com.halfgallon.withcon.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisStringServiceImpl implements RedisStringService {

  private final RedisTemplate<String, String> redisTemplate;

  @Override
  public String get(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  @Override
  public void save(String key, String value) {
    try {
      redisTemplate.opsForValue().set(key, value);
    } catch (Exception e) {
      throw new CustomException(REDIS_SUBSCRIBE_FAIL);
    }
  }

  @Override
  public void delete(String key) {
    try {
      redisTemplate.delete(key);
    } catch (Exception e) {
      log.info("redis 삭제 실패 {}", key);
    }
  }
}
