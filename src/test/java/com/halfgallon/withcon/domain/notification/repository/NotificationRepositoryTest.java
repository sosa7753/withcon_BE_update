package com.halfgallon.withcon.domain.notification.repository;

import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.notification.constant.NotificationType;
import com.halfgallon.withcon.domain.notification.entity.Notification;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest
@WebAppConfiguration
class NotificationRepositoryTest {

  private static final int MAX = 1000;

  @Autowired
  NotificationRepository notificationRepository;

  @Test
  @DisplayName("save 저장")
  void singleSaveTest() {

    long start = System.currentTimeMillis();

    for(int i=1; i<=MAX; i++) {
      Notification notification = Notification.builder()
          .member(makeMember((long) i))
          .notificationType(NotificationType.CHATROOM)
          .message("test 알림")
          .build();

      notificationRepository.save(notification);
    }
    System.out.println("실행 시간: " + (System.currentTimeMillis() - start) + "ms");
  }

  @Test
  @DisplayName("saveAll 저장")
  void multiSaveTest() {

    long start = System.currentTimeMillis();
    List<Notification> notifications = new ArrayList<>();

    for(int i=1; i<=MAX; i++) {
      Notification notification = Notification.builder()
          .member(makeMember((long) i))
          .notificationType(NotificationType.CHATROOM)
          .message("test 알림")
          .build();

      notifications.add(notification);
    }

    notificationRepository.saveAll(notifications);
    System.out.println("실행 시간: " + (System.currentTimeMillis() - start) + "ms");
  }

  private Member makeMember(Long memberId) {
    return Member.builder()
        .id(memberId)
        .build();
  }
}