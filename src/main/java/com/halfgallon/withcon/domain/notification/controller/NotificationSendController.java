package com.halfgallon.withcon.domain.notification.controller;

import com.halfgallon.withcon.domain.notification.dto.NotificationResponse;
import com.halfgallon.withcon.domain.notification.service.SseEmitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationSendController {

  private final SseEmitterService sseEmitterService;

  @PostMapping("/send")
  public ResponseEntity<Void> sendNotification(
      @RequestBody NotificationResponse notificationResponse) {

     sseEmitterService.sendNotificationToClient(notificationResponse);
     return ResponseEntity.ok().build();
  }
}
