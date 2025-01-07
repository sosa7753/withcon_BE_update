package com.halfgallon.withcon.domain.notification.kafka.consumer.notification;

import static com.halfgallon.withcon.domain.notification.kafka.constant.KafkaTopic.NOTIFICATION;

import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.member.repository.MemberRepository;
import com.halfgallon.withcon.domain.notification.constant.NotificationType;
import com.halfgallon.withcon.domain.notification.entity.Notification;
import com.halfgallon.withcon.domain.notification.kafka.dto.ChatRoomNotificationKafkaRequest;
import com.halfgallon.withcon.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationSaver {

  private final MemberRepository memberRepository;
  private final NotificationRepository notificationRepository;

  @KafkaListener(topics = NOTIFICATION, containerFactory = "notificationSaveListenerContainerFactory", concurrency = "2")
  public void listener(ConsumerRecord<String, Object> record) {
    ChatRoomNotificationKafkaRequest kafkaRequest = (ChatRoomNotificationKafkaRequest) record.value();

    // 유저가 탈퇴 했다면 무시
    Member member = memberRepository.findById(kafkaRequest.getMemberId()).orElse(null);
    if (member == null) {
      return;
    }

    Notification notification = Notification.builder()
        .message(kafkaRequest.getMessage())
        .url(kafkaRequest.getUrl())
        .notificationType(NotificationType.CHATROOM)
        .createdAt(kafkaRequest.getCreatedAt())
        .member(member)
        .build();
    notificationRepository.save(notification);
    log.info("알림 저장 성공");
  }
}
