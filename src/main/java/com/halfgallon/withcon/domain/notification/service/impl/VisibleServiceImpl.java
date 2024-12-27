package com.halfgallon.withcon.domain.notification.service.impl;

import com.halfgallon.withcon.domain.notification.constant.RedisCacheType;
import com.halfgallon.withcon.domain.notification.dto.VisibleRequest;
import com.halfgallon.withcon.domain.notification.service.RedisService;
import com.halfgallon.withcon.domain.notification.service.VisibleService;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VisibleServiceImpl implements VisibleService {

  private final RedisService redisService;

  // Redis에 visible 데이터 저장.
  public void createVisibleCache(Long memberId, VisibleRequest request) {
    String hashKey = RedisCacheType.VISIBLE_CACHE.getDescription() + request.getChatRoomId();
    log.info("Visible 채널 명 : {}", hashKey);

    Map<Object, Object> visibleCaches = redisService.getHashByKey(hashKey);
    log.info("Hash 데이터 조회 : {}", visibleCaches);

    if (visibleCaches != null) { // 특정 채팅방의 Map이 이미 존재한다면
      redisService.updateToHash(hashKey, String.valueOf(memberId), request.getVisibleType());
    } else {
      Map<Object, Object> newObject = new HashMap<>();
      newObject.put(String.valueOf(memberId), request.getVisibleType());
      redisService.saveToHash(hashKey, newObject, 24);
    }
  }
}
