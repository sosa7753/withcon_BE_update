package com.halfgallon.withcon.domain.notification.dto;

import com.halfgallon.withcon.domain.notification.entity.Notification;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationResponse {

  private Long memberId;
  private String message;
  private String url;
  private LocalDateTime createdAt;

  public static NotificationResponse from(Notification notification) {
    return NotificationResponse.builder()
        .memberId(notification.getMember().getId())
        .message(notification.getMessage())
        .url(notification.getUrl())
        .createdAt(notification.getCreatedAt())
        .build();
  }
}
