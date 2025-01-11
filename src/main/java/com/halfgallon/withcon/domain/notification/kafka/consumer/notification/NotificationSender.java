package com.halfgallon.withcon.domain.notification.kafka.consumer.notification;

import static com.halfgallon.withcon.domain.notification.kafka.constant.KafkaTopic.NOTIFICATION;

import com.halfgallon.withcon.domain.notification.dto.NotificationResponse;
import com.halfgallon.withcon.domain.notification.kafka.dto.ChatRoomNotificationKafkaRequest;
import com.halfgallon.withcon.domain.notification.service.SseEmitterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationSender {

  private final SseEmitterService sseEmitterService;

  @KafkaListener(topics = NOTIFICATION, containerFactory = "notificationSendListenerContainerFactory", concurrency = "2")
  public void listener(ConsumerRecord<String, Object> record) {
    ChatRoomNotificationKafkaRequest kafkaRequest = (ChatRoomNotificationKafkaRequest) record.value();

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