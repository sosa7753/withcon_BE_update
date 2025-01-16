package com.halfgallon.withcon.domain.notification.kafka.consumer.notification;

import static com.halfgallon.withcon.domain.notification.kafka.constant.KafkaTopic.NOTIFICATION;

import com.halfgallon.withcon.domain.notification.dto.NotificationResponse;
import com.halfgallon.withcon.domain.notification.kafka.dto.ChatRoomNotificationKafkaRequest;
import com.halfgallon.withcon.domain.notification.service.redis.service.RedisStringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationSender {

  private final WebClient webClient;
  private final RedisStringService redisStringService;

  @KafkaListener(topics = NOTIFICATION, containerFactory = "notificationSendListenerContainerFactory", concurrency = "2")
  public void listener(ConsumerRecord<String, Object> record) {
    ChatRoomNotificationKafkaRequest kafkaRequest = (ChatRoomNotificationKafkaRequest) record.value();

    String serverIp = redisStringService.get(String.valueOf(kafkaRequest.getMemberId()));
    if(serverIp == null) {
      return;
    }

    NotificationResponse notificationResponse =
        NotificationResponse.builder()
            .memberId(kafkaRequest.getMemberId())
            .message(kafkaRequest.getMessage())
            .url(kafkaRequest.getUrl())
            .createdAt(kafkaRequest.getCreatedAt())
            .build();

    webClient.post()
        .uri("http://" + serverIp + "/notification/send")
        .bodyValue(notificationResponse)
        .retrieve()
        .toBodilessEntity()
        .doOnSuccess(response -> log.info("알림 전송 성공 {}", notificationResponse.getMemberId()))
        .doOnError(error -> log.error("알림 전송 실패 {}", notificationResponse.getMemberId()))
        .subscribe();
  }
}