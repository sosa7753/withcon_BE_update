package com.halfgallon.withcon.domain.notification.kafka.consumer.notification;

import static com.halfgallon.withcon.domain.notification.kafka.constant.KafkaTopic.NOTIFICATION;

import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.member.repository.MemberRepository;
import com.halfgallon.withcon.domain.notification.constant.NotificationType;
import com.halfgallon.withcon.domain.notification.entity.Notification;
import com.halfgallon.withcon.domain.notification.kafka.dto.ChatRoomNotificationKafkaArrayRequest;
import com.halfgallon.withcon.domain.notification.repository.NotificationRepository;
import java.util.ArrayList;
import java.util.List;
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
    ChatRoomNotificationKafkaArrayRequest kafkaRequest = (ChatRoomNotificationKafkaArrayRequest) record.value();

    List<Member> members = memberRepository.findByIdIn(kafkaRequest.getMembers());

    List<Notification> notifications = new ArrayList<>();
    for(Member member : members) {
      Notification notification = Notification.builder()
          .message(kafkaRequest.getMessage())
          .url(kafkaRequest.getUrl())
          .notificationType(NotificationType.CHATROOM)
          .createdAt(kafkaRequest.getCreatedAt())
          .member(member)
          .build();

      notifications.add(notification);
    }

    notificationRepository.saveAll(notifications);
    log.info("알림 저장 성공");
  }
}
