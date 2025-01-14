package com.halfgallon.withcon.domain.notification.service.impl;

import static com.halfgallon.withcon.domain.notification.constant.NotificationMessage.SUBSCRIBE;
import static com.halfgallon.withcon.global.exception.ErrorCode.NOT_EXIST_NOTIFICATION;

import com.halfgallon.withcon.domain.notification.dto.NotificationResponse;
import com.halfgallon.withcon.domain.notification.entity.Notification;
import com.halfgallon.withcon.domain.notification.repository.NotificationRepository;
import com.halfgallon.withcon.domain.notification.repository.SseEmitterRepository;
import com.halfgallon.withcon.domain.notification.service.NotificationService;
import com.halfgallon.withcon.domain.notification.service.SseEmitterService;
import com.halfgallon.withcon.domain.notification.service.redis.service.RedisStringService;
import com.halfgallon.withcon.global.exception.CustomException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  private static final long TIME_OUT = 30L * 1000 * 60;

  private final NotificationRepository notificationRepository;
  private final SseEmitterRepository sseEmitterRepository;
  private final SseEmitterService sseEmitterService;

  private final RedisStringService redisStringService;

  @Value("${server.port}")
  private int port;

  @Override
  public SseEmitter subscribe(Long memberId) {
    redisStringService.save(String.valueOf(memberId), "localhost:" + port);

    String emitterId = createEmitterId(memberId);
    SseEmitter sseEmitter = sseEmitterRepository.save(emitterId, new SseEmitter(TIME_OUT));

    // 더미 데이터(503 에러 방지)
    sseEmitterService.send(sseEmitter, emitterId,
        SUBSCRIBE.getDescription() + memberId + "\"}");
    log.info("SSE 구독 완료");

    sseEmitter.onCompletion(() -> {
      log.info("onCompletion callback");
      sseEmitterRepository.deleteById(emitterId);
      redisStringService.delete(String.valueOf(memberId));
    });
    sseEmitter.onError((e) -> {
      log.info("onError callback");
      sseEmitter.complete();
    });
    sseEmitter.onTimeout(() -> {
      log.info("onTimeout callback");
      sseEmitterRepository.deleteById(emitterId);
      redisStringService.delete(String.valueOf(memberId));
    });

    return sseEmitter;
  }

  private String createEmitterId(Long memberId) {
    return memberId + "_" + System.currentTimeMillis();
  }

  @Override
  @Transactional(readOnly = true)
  public List<NotificationResponse> findNotification(Long memberId) {
    List<Notification> notifications =
        notificationRepository.findNotificationsByMember_IdAndReadStatus(memberId, false);
    log.info("Service : 알림 조회 완료");

    return notifications.stream().map(NotificationResponse::from)
        .collect(Collectors.toList());
  }

  @Override
  public void readNotification(Long notificationId) {
    Notification notification =
        notificationRepository.findById(notificationId)
            .orElseThrow(() -> new CustomException(NOT_EXIST_NOTIFICATION));

    notification.updateReadStatus();
    notificationRepository.save(notification);
    log.info("Service : 알림 읽음");
  }
}
