package com.halfgallon.withcon.domain.notification.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface Consumer {

  void listener(ConsumerRecord<String, Object> record);

}
