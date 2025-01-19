package com.halfgallon.withcon.domain.notification.service.redis.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.halfgallon.withcon.domain.notification.service.redis.service.RedisStringService;
import java.util.ArrayList;
import java.util.List;
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
  private static final int MAX = 1000;

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

  @Test
  @DisplayName("redis 단건 순회 조회")
  void redisSingleKeyGetTest() {
    // given
    for (int i = 1; i <= MAX; i++) {
      redisStringService.save(String.valueOf(i), "test");
    }

    long startTime = System.currentTimeMillis();

    for (int i = 1; i <= MAX; i++) {
      String s = redisStringService.get(String.valueOf(i));
    }

    System.out.println("실행 시간: " + (System.currentTimeMillis() - startTime) + "ms");
  }

  @Test
  @DisplayName("redis 다건 조회")
  void redisMultiGetTest() {
    //given
    List<Long> members = new ArrayList<>();
    for (int i = 1; i <= MAX; i++) {
      redisStringService.save(String.valueOf(i), "test");
      members.add((long) i);
    }

    List<ArrayList<Long>> list = new ArrayList<>();
    int div = MAX/5;
    for(int i=0; i<=div; i++) {
      list.add(new ArrayList<>());
    }

    int row = 0;
    int col = 0;
    for(int i=0; i<MAX; i++) {
      list.get(row).add(members.get(i));
      col++;
      if(col%div==0) {
        row++;
      }
    }

    long startTime = System.currentTimeMillis();

    for(int i=0; i<list.size(); i++) {
      List<String> value = redisStringService.multiGet(list.get(i));
    }

    System.out.println("실행 시간: " + (System.currentTimeMillis() - startTime) + "ms");
  }
}