package com.halfgallon.withcon.domain.notification.kafka.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConsumerGroup {

  NOTIFICATION_SEND("notification_send"),
  NOTIFICATION_SAVE("notification_save");

  private final String name;
}
