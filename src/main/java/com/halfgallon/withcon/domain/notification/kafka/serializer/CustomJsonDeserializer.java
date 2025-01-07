package com.halfgallon.withcon.domain.notification.kafka.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halfgallon.withcon.domain.notification.kafka.dto.ChatRoomNotificationKafkaRequest;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class CustomJsonDeserializer extends JsonDeserializer<ChatRoomNotificationKafkaRequest> {

  public CustomJsonDeserializer() {
    super();
  }

  public CustomJsonDeserializer(ObjectMapper objectMapper) {
    super(objectMapper);
  }
}
