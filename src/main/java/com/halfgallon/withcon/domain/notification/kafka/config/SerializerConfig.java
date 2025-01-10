package com.halfgallon.withcon.domain.notification.kafka.config;

import com.halfgallon.withcon.domain.notification.kafka.serializer.CustomJsonDeserializer;
import com.halfgallon.withcon.domain.notification.kafka.serializer.CustomJsonSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SerializerConfig {

  @Bean
  public CustomJsonSerializer customJsonSerializer() {
    return new CustomJsonSerializer();
  }

  @Bean
  public CustomJsonDeserializer customJsonDeserializer() {
    return new CustomJsonDeserializer();
  }
}
