package com.halfgallon.withcon.domain.notification.service.redis.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.halfgallon.withcon.domain.notification.service.redis.service.RedisStringService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest
@WebAppConfiguration
class RedisStringServiceImplTest {

  @Autowired
  private RedisStringService redisStringService;

  private static final String KEY = "test";
  private static final String VALUE = "Hello";

  @Test
  @DisplayName("redis String 저장 조회 성공")
  void redisSaveOrGetTest() {
    redisStringService.save(KEY, VALUE);
    assertEquals(VALUE, redisStringService.get(KEY));
  }

  @Test
  @DisplayName("redis String 삭제")
  void redisDeleteTest() {
    redisStringService.save(KEY, VALUE);
    redisStringService.delete(KEY);
    assertNull(redisStringService.get(KEY));
  }
}