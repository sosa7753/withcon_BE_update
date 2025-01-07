package com.halfgallon.withcon.domain.notification.kafka.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.halfgallon.withcon.domain.notification.kafka.serializer.CustomJsonDeserializer;
import com.halfgallon.withcon.domain.notification.kafka.serializer.CustomJsonSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SerializerConfig {

  @Bean
  public CustomJsonSerializer customJsonSerializer() {
    return new CustomJsonSerializer(customObjectMapper());
  }

  @Bean
  public CustomJsonDeserializer customJsonDeserializer() {
    return new CustomJsonDeserializer(customObjectMapper());
  }

  @Bean
  public ObjectMapper customObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);
    return objectMapper;
  }
}
