package com.halfgallon.withcon.domain.notification.service.impl;

import static com.halfgallon.withcon.global.exception.ErrorCode.REDIS_SUBSCRIBE_FAIL;

import com.halfgallon.withcon.domain.notification.dto.NotificationResponse;
import com.halfgallon.withcon.domain.notification.service.RedisNotificationService;
import com.halfgallon.withcon.domain.notification.service.handler.RedisListener;
import com.halfgallon.withcon.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisNotificationServiceImpl implements RedisNotificationService {

  private final RedisMessageListenerContainer container;
  private final RedisListener subscriber;
  private final RedisTemplate<String, Object> redisTemplate;

  @Override
  public void subscribe(String channel) {
    try {
      container.addMessageListener(subscriber, ChannelTopic.of(channel));
      log.info("redis 채널 구독 성공");
    }catch (Exception e){
      log.info("redis 채널 구독 실패");
      throw new CustomException(REDIS_SUBSCRIBE_FAIL);
    }
  }

  @Override
  public void unsubscribe(String channel) {
    container.removeMessageListener(subscriber, ChannelTopic.of(channel));
    log.info("redis 채널 구독 해지");
  }

  // 특정 채널에 메세지 발행
  @Override
  public void publish(String channel, NotificationResponse notificationResponse) {
    redisTemplate.convertAndSend(channel, notificationResponse);
    log.info("redis 메세지 발행 성공");
  }

}


