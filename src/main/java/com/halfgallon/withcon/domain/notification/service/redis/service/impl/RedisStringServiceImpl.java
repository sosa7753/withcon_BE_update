package com.halfgallon.withcon.domain.notification.service.redis.service.impl;

import static com.halfgallon.withcon.global.exception.ErrorCode.REDIS_SUBSCRIBE_FAIL;

import com.halfgallon.withcon.domain.notification.service.redis.service.RedisStringService;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import com.halfgallon.withcon.global.exception.CustomException;
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
  public List<String> multiGet(List<Long> keys) {
    List<String> stringKeys = keys.stream().map(String::valueOf)
            .toList();
    return redisTemplate.opsForValue().multiGet(stringKeys);
  }

  @Override
  public void save(String key, String value) {
    try {
      redisTemplate.opsForValue().set(key, value);
      log.info("공유 메모리 key : {}, value : {}", key, value);
      redisTemplate.expire(key, ttl);
      log.info("공유 메모리 key : {}, ttl : {}", key, ttl);
    } catch (Exception e) {
      throw new CustomException(REDIS_SUBSCRIBE_FAIL);
    }
  }

  @Override
  public void delete(String key) {
    try {
      redisTemplate.delete(key);
    } catch (Exception e) {
      log.info("공유 메모리 삭제 실패 {}", key);
    }
  }
}
