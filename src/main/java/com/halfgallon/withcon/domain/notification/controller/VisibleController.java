package com.halfgallon.withcon.domain.notification.controller;

import com.halfgallon.withcon.domain.auth.security.service.CustomUserDetails;
import com.halfgallon.withcon.domain.notification.dto.VisibleRequest;
import com.halfgallon.withcon.domain.notification.service.VisibleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class VisibleController {

  private final VisibleService visibleService;

  // 회원이 채팅방을 보고 있는지 여부 전달
  @PostMapping("/visible")
  public ResponseEntity<Void> visibleChatRoom(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @RequestBody @Valid VisibleRequest request) {

    visibleService.createVisibleCache(
        customUserDetails.getId(), request);
    return ResponseEntity.ok().build();
  }
}
