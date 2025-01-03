package com.halfgallon.withcon.domain.notification.kafka.config;

import static com.halfgallon.withcon.domain.notification.kafka.constant.KafkaTopic.*;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfig {

  @Bean
  public NewTopic notificationTopic() {
    return TopicBuilder.name(NOTIFICATION)
        .partitions(2)
        .build();
  }
}
