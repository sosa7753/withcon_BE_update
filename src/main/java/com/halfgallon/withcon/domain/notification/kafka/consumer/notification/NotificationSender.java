package com.halfgallon.withcon.domain.notification.kafka.consumer.notification;

import static com.halfgallon.withcon.domain.notification.kafka.constant.KafkaTopic.NOTIFICATION;

import com.halfgallon.withcon.domain.notification.dto.NotificationResponses;
import com.halfgallon.withcon.domain.notification.kafka.dto.ChatRoomNotificationKafkaArrayRequest;
import com.halfgallon.withcon.domain.notification.service.redis.service.RedisStringService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

  private static final Map<String, List<Long>> SERVER = new HashMap<>();

  private final WebClient webClient;
  private final RedisStringService redisStringService;

  @KafkaListener(topics = NOTIFICATION, containerFactory = "notificationSendListenerContainerFactory", concurrency = "2")
  public void listener(ConsumerRecord<String, Object> record) {
    ChatRoomNotificationKafkaArrayRequest kafkaRequest = (ChatRoomNotificationKafkaArrayRequest) record.value();

    List<Long> members = kafkaRequest.getMembers();
    List<String> serverIps = redisStringService.multiGet(members);

    for (int i = 0; i < members.size(); i++) {
      String serverIp = serverIps.get(i);
      if (serverIp == null) {
        continue;
      }

      List<Long> list = SERVER.get(serverIp);
      if (list == null) {
        list = new ArrayList<>();
        SERVER.put(serverIp, list);
      }
      list.add(members.get(i));
    }

    for (Map.Entry<String, List<Long>> entry : SERVER.entrySet()) {
      NotificationResponses notificationResponses =
          NotificationResponses.builder()
              .memberIds(entry.getValue())
              .message(kafkaRequest.getMessage())
              .url(kafkaRequest.getUrl())
              .createdAt(kafkaRequest.getCreatedAt())
              .build();

//      sseEmitterService.sendMultiNotificationToClient(notificationResponses);

      webClient.post()
          .uri("http://" + entry.getKey() + "/notifications/send")
          .bodyValue(notificationResponses)
          .retrieve()
          .toBodilessEntity()
          .doOnSuccess(response -> log.info("알림 전송 성공 서버: {}", entry.getKey()))
          .doOnError(error -> log.error("알림 전송 실패 서버: {}", entry.getKey()))
          .subscribe();
    }
  }
}