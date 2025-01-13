package com.halfgallon.withcon.domain.notification.service.redis.service;

import com.halfgallon.withcon.domain.notification.dto.NotificationResponse;

public interface RedisNotificationService {
  void subscribe(String channel); // 채널 구독

  void unsubscribe(String channel); // 구독 해지

  void publish(String channel, NotificationResponse notificationResponse); // 메세지 발행
}
