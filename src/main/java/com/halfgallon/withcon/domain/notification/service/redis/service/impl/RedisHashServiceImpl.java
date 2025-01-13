package com.halfgallon.withcon.domain.notification.service.redis.service.impl;

import com.halfgallon.withcon.domain.chat.dto.ChatRoomSessionDto;
import com.halfgallon.withcon.domain.notification.service.redis.service.RedisHashService;
import java.time.Duration;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisHashServiceImpl implements RedisHashService {

  private final RedisTemplate<String, Object> redisTemplate;

  @Override
  public Map<String, Object> getHashByKey(String key) {
    HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();
    return hashOps.entries(key);
  }

  @Override
  public void saveToHash(String hashKey, Map<String, Object> data, int hourTime) {
    redisTemplate.opsForHash().putAll(hashKey, data);

    redisTemplate.expire(hashKey, Duration.ofHours(hourTime));
  }

  @Override
  public void updateToHash(String hashKey, String field, Object value) {
    redisTemplate.opsForHash().put(hashKey, field, value);
  }

  @Override
  public ChatRoomSessionDto getChatRoomHashKey(String key, String sessionId) {
    HashOperations<String, Object, ChatRoomSessionDto> hashOps = redisTemplate.opsForHash();
    return hashOps.get(key, sessionId);
  }

  @Override
  public void deleteHashKey(String hashKey, Object value) {
    redisTemplate.opsForHash().delete(hashKey, value);
  }
}
