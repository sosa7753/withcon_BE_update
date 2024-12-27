package com.halfgallon.withcon.domain.notification.controller;

import com.halfgallon.withcon.domain.auth.security.service.CustomUserDetails;
import com.halfgallon.withcon.domain.notification.dto.ChatRoomNotificationRequest;
import com.halfgallon.withcon.domain.notification.dto.NotificationResponse;
import com.halfgallon.withcon.domain.notification.service.NotificationService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  // 알림 구독
  @GetMapping(value = "/notification/subscribe", produces = "text/event-stream; charset=UTF-8")
  public ResponseEntity<SseEmitter> subscribe(
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    return ResponseEntity.ok(
        notificationService.subscribe(customUserDetails.getId()));
  }

  // 채팅방 입장/퇴장/강퇴 알림 발생
  @PostMapping("/notification/chatRoom-event")
  public ResponseEntity<Void> createNotification(
      @RequestBody @Valid ChatRoomNotificationRequest request) {

    notificationService.createNotificationChatRoom(request);
    return ResponseEntity.ok().build();
  }

  // 나에게 온 알림 조회
  @GetMapping("/notifications")
  public ResponseEntity<List<NotificationResponse>> findNotification(
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    return ResponseEntity.ok(
        notificationService.findNotification(customUserDetails.getId()));
  }

  // 알림 읽음
  @PutMapping("/notification/{notificationId}")
  public ResponseEntity<Void> readNotification(
      @PathVariable Long notificationId) {

    notificationService.readNotification(notificationId);
    return ResponseEntity.ok().build();
  }
}
