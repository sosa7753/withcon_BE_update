package com.halfgallon.withcon.domain.notification.repository.impl;

import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.notification.constant.NotificationType;
import com.halfgallon.withcon.domain.notification.entity.Notification;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

@ActiveProfiles("test")
@SpringBootTest
@WebAppConfiguration
class BatchNotificationRepositoryTest {

  private static final int MAX = 1000;

  @Autowired
  BatchNotificationRepository batchNotificationRepository;

  @Test
  @DisplayName("BatchInsert 저장")
  void batchSaveTest() {

    long start = System.currentTimeMillis();
    List<Notification> notifications = new ArrayList<>();

    for(int i=1; i<=MAX; i++) {
      Notification notification = Notification.builder()
          .member(makeMember((long) i))
          .notificationType(NotificationType.CHATROOM)
          .message("test 알림")
          .createdAt(LocalDateTime.now())
          .readStatus(false)
          .url("test")
          .build();

      notifications.add(notification);
    }
    batchNotificationRepository.saveAll(notifications);
    System.out.println("실행 시간: " + (System.currentTimeMillis() - start) + "ms");
  }

  private Member makeMember(Long memberId) {
    return Member.builder()
        .id(memberId)
        .build();
  }
}