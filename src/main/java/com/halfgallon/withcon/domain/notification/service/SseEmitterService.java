package com.halfgallon.withcon.domain.notification.service;

import com.halfgallon.withcon.domain.notification.dto.NotificationResponse;
import com.halfgallon.withcon.domain.notification.dto.NotificationResponses;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseEmitterService {

  void sendNotificationToClient(NotificationResponse notificationResponse);
  void sendMultiNotificationToClient(NotificationResponses notificationResponses);
  void send(SseEmitter sseEmitter, String emitterId, Object data);

}
