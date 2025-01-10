package com.halfgallon.withcon.domain.notification.kafka.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.halfgallon.withcon.domain.notification.kafka.dto.ChatRoomNotificationKafkaRequest;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class CustomJsonSerializer extends JsonSerializer<ChatRoomNotificationKafkaRequest> {

  public CustomJsonSerializer() {
    super(customObjectMapper());
  }

  private static ObjectMapper customObjectMapper() {
    ObjectMapper mapper = JacksonUtils.enhancedObjectMapper();
    mapper.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);
    return mapper;
  }
}
