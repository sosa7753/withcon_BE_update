package com.halfgallon.withcon.domain.notification.kafka.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halfgallon.withcon.domain.notification.kafka.dto.ChatRoomNotificationKafkaRequest;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class CustomJsonSerializer extends JsonSerializer<ChatRoomNotificationKafkaRequest> {

  public CustomJsonSerializer() {
    super();
  }

  public CustomJsonSerializer(ObjectMapper objectMapper) {
    super(objectMapper);
  }
}
