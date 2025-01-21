package com.halfgallon.withcon.domain.notification.event.handler;

import com.halfgallon.withcon.domain.notification.dto.ChatMessageNotificationRequest;
import com.halfgallon.withcon.domain.notification.event.NewMessageEvent;
import com.halfgallon.withcon.domain.notification.kafka.producer.ChatMessageNotificationProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewMessageEventHandler {
  private final ChatMessageNotificationProducer chatMessageNotificationProducer;

  @Async
  @EventListener
  public void handleNewMessageEvent(NewMessageEvent event) {
    ChatMessageNotificationRequest request =
        ChatMessageNotificationRequest.builder()
            .chatRoomId(event.getChatRoomId())
            .build();

    chatMessageNotificationProducer.send(request);
  }
}
