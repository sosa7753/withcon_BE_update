package com.halfgallon.withcon.domain.notification.controller.event;

import com.halfgallon.withcon.domain.notification.service.handler.GenerateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class ChatRoomEventController {

  private final GenerateEvent event;

  // 채팅방 메시지 발생 event
  @PostMapping("/event")
  public ResponseEntity<Void> generateEvent(
      @RequestParam Long chatRoomId) {

    event.doSomething(chatRoomId);
    return ResponseEntity.ok().build();
  }
}
