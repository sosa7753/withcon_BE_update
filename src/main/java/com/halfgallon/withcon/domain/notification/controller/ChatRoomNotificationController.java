package com.halfgallon.withcon.domain.notification.controller;

import com.halfgallon.withcon.domain.notification.dto.ChatRoomNotificationRequest;
import com.halfgallon.withcon.domain.notification.kafka.constant.NotificationProducerType;
import com.halfgallon.withcon.domain.notification.service.impl.GeneralNotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("notification")
@RequiredArgsConstructor
public class ChatRoomNotificationController {

  private final GeneralNotificationService generalNotificationService;

  // 채팅방 입장/퇴장/강퇴 알림 발생
  @PostMapping("/chatRoom-event")
  public ResponseEntity<Void> createChatRoomNotification(
      @RequestBody @Valid ChatRoomNotificationRequest request) {

    generalNotificationService.produce(NotificationProducerType.CHATROOM_USER, request);
    return ResponseEntity.ok().build();
  }
}
