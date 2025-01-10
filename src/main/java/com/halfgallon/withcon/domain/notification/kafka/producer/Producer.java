package com.halfgallon.withcon.domain.notification.kafka.producer;

import com.halfgallon.withcon.domain.notification.kafka.constant.NotificationProducerType;

public interface Producer<T> {
  void send(T request);
  NotificationProducerType getType();
}

