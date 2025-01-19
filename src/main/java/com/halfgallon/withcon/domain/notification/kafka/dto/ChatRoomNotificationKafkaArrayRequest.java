package com.halfgallon.withcon.domain.notification.kafka.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatRoomNotificationKafkaArrayRequest {

  private List<Long> members;
  private String message;
  private String url;
  private LocalDateTime createdAt;

}
