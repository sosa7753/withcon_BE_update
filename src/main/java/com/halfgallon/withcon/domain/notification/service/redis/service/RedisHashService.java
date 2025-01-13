package com.halfgallon.withcon.domain.notification.service.redis.service;

import com.halfgallon.withcon.domain.chat.dto.ChatRoomSessionDto;
import java.util.Map;

public interface RedisHashService {

  // hashKey로 조회
  Map<String, Object> getHashByKey(String key);

  // HashKey - Map 구조 저장
  void saveToHash(String hashKey, Map<String, Object> data, int hourTime);

  // HashKey - Map 값 변경
  void updateToHash(String hashKey, String field, Object value);

  ChatRoomSessionDto getChatRoomHashKey(String key, String sessionId);

  void deleteHashKey(String hashKey, Object value);
}
