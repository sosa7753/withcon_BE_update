package com.halfgallon.withcon.domain.notification.controller;

import com.halfgallon.withcon.domain.notification.dto.NotificationResponse;
import com.halfgallon.withcon.domain.notification.dto.NotificationResponses;
import com.halfgallon.withcon.domain.notification.service.SseEmitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequiredArgsConstructor
public class NotificationSendController {

  private final SseEmitterService sseEmitterService;

  @PostMapping("/notification/send")
  public ResponseEntity<Void> sendNotification(
      @RequestBody NotificationResponse notificationResponse) {

     sseEmitterService.sendNotificationToClient(notificationResponse);
     return ResponseEntity.ok().build();
  }

  @PostMapping("notifications/send")
  public ResponseEntity<Void> sendNotifications(
      @RequestBody NotificationResponses notificationResponses) {

    sseEmitterService.sendMultiNotificationToClient(notificationResponses);
    return ResponseEntity.ok().build();
  }
}
