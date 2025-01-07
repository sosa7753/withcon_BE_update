package com.halfgallon.withcon.domain.notification.kafka.consumer.notification;

import static com.halfgallon.withcon.domain.notification.kafka.constant.KafkaTopic.NOTIFICATION;

import com.halfgallon.withcon.domain.notification.constant.RedisCacheType;
import com.halfgallon.withcon.domain.notification.constant.VisibleType;
import com.halfgallon.withcon.domain.notification.dto.NotificationResponse;
import com.halfgallon.withcon.domain.notification.kafka.consumer.Consumer;
import com.halfgallon.withcon.domain.notification.kafka.dto.ChatRoomNotificationKafkaRequest;
import com.halfgallon.withcon.domain.notification.service.RedisService;
import com.halfgallon.withcon.domain.notification.service.SseEmitterService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationSender implements Consumer {

  private final SseEmitterService sseEmitterService;
  private final RedisService redisService;

  @KafkaListener(topics = NOTIFICATION, containerFactory = "notificationSendListenerContainerFactory", concurrency = "2")
  @Override
  public void listener(ConsumerRecord<String, Object> record) {
    ChatRoomNotificationKafkaRequest kafkaRequest = (ChatRoomNotificationKafkaRequest) record.value();

    String visibleKey = RedisCacheType.VISIBLE_CACHE.getDescription()
        + kafkaRequest.getChatRoomId();
    log.info("채널 KEY : {}", visibleKey);

    Map<String, Object> cache = redisService.getHashByKey(visibleKey);
    log.info("Visible 캐시 데이터 조회 : {} ", cache);
    if (cache == null) {
      return;
    }

    if (!cache.containsKey(String.valueOf(kafkaRequest.getMemberId()))) {
      return;
    }

    VisibleType visibleType = VisibleType.valueOf(
        (String) cache.get(String.valueOf(kafkaRequest.getMemberId())));

    if (visibleType == VisibleType.HIDDEN || visibleType == VisibleType.NONE) {
      NotificationResponse notificationResponse =
          NotificationResponse.builder()
              .memberId(kafkaRequest.getMemberId())
              .message(kafkaRequest.getMessage())
              .url(kafkaRequest.getUrl())
              .createdAt(kafkaRequest.getCreatedAt())
              .build();
      sseEmitterService.sendNotificationToClient(notificationResponse);
    }
  }
}