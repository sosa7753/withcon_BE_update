package com.halfgallon.withcon.domain.notification.kafka.config;

import static com.halfgallon.withcon.domain.notification.kafka.constant.ConsumerGroup.NOTIFICATION_SAVE;
import static com.halfgallon.withcon.domain.notification.kafka.constant.ConsumerGroup.NOTIFICATION_SEND;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;


@Configuration
@EnableKafka
public class ConsumerKafkaConfig {

  // 알림 전송 컨슈머 그룹
  @Bean
  public ConsumerFactory<String, Object> notificationSendFactory() {
    Map<String, Object> config = new HashMap<>();
    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    config.put(ConsumerConfig.GROUP_ID_CONFIG, NOTIFICATION_SEND.getName()); // 같은 토픽을 소비하는 Consumer 그룹의 ID
    config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    return new DefaultKafkaConsumerFactory<>(config);
  }

  // 알림 저장 컨슈머 그룹
  @Bean
  public ConsumerFactory<String, Object> notificationSaveFactory() {
    Map<String, Object> config = new HashMap<>();
    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    config.put(ConsumerConfig.GROUP_ID_CONFIG, NOTIFICATION_SAVE.getName()); // 같은 토픽을 소비하는 Consumer 그룹의 ID
    config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    return new DefaultKafkaConsumerFactory<>(config);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, Object> notificationSendListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(notificationSendFactory());
    return factory;
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, Object> notificationSaveListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(notificationSaveFactory());
    return factory;
  }
}
