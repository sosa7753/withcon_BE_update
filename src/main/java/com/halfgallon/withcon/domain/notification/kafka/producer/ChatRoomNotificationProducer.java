package com.halfgallon.withcon.domain.notification.kafka.producer;

import static com.halfgallon.withcon.domain.chat.constant.MessageType.ENTER;
import static com.halfgallon.withcon.domain.chat.constant.MessageType.EXIT;
import static com.halfgallon.withcon.domain.chat.constant.MessageType.KICK;
import static com.halfgallon.withcon.domain.notification.constant.NotificationMessage.ENTER_CHATROOM;
import static com.halfgallon.withcon.domain.notification.constant.NotificationMessage.EXIT_CHATROOM;
import static com.halfgallon.withcon.domain.notification.constant.NotificationMessage.KICK_CHATROOM;
import static com.halfgallon.withcon.global.exception.ErrorCode.INVALID_PARAMETER;

import com.halfgallon.withcon.domain.chat.constant.MessageType;
import com.halfgallon.withcon.domain.chat.entity.ChatParticipant;
import com.halfgallon.withcon.domain.chat.repository.ChatParticipantRepository;
import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.member.repository.MemberRepository;
import com.halfgallon.withcon.domain.notification.constant.NotificationType;
import com.halfgallon.withcon.domain.notification.constant.RedisCacheType;
import com.halfgallon.withcon.domain.notification.constant.VisibleType;
import com.halfgallon.withcon.domain.notification.dto.ChatRoomNotificationRequest;
import com.halfgallon.withcon.domain.notification.kafka.constant.KafkaTopic;
import com.halfgallon.withcon.domain.notification.kafka.constant.NotificationProducerType;
import com.halfgallon.withcon.domain.notification.kafka.dto.ChatRoomNotificationKafkaRequest;
import com.halfgallon.withcon.domain.notification.service.RedisService;
import com.halfgallon.withcon.global.exception.CustomException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatRoomNotificationProducer implements Producer<ChatRoomNotificationRequest> {

  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final ChatParticipantRepository chatParticipantRepository;
  private final MemberRepository memberRepository;
  private final RedisService redisService;

  @Override
  public void send(ChatRoomNotificationRequest request) {
    List<ChatParticipant> chatParticipants = getChatParticipants(
        request.getChatRoomId());

    Map<String, Object> cache = getRedisCache(request.getChatRoomId());
    if (cache == null) {
      return;
    }

    Member target = memberRepository.findById(request.getTargetId())
        .orElse(withdrawMember());

    String message = createChatRoomMessage(target.getNickname(), request.getMessageType());
    String url = createChatRoomUrl(request.getChatRoomId());

    for (ChatParticipant chatParticipant : chatParticipants) {
      Member member = chatParticipant.getMember();
      if (member.getId().equals(request.getTargetId())) {
        log.info("target 제외 : {}", request.getTargetId());
        continue;
      }

      Object visible = cache.get(String.valueOf(member.getId()));
      if (visible != null && (VisibleType.valueOf((String) visible) == VisibleType.HIDDEN ||
              VisibleType.valueOf((String) visible) == VisibleType.NONE)) {

        ChatRoomNotificationKafkaRequest kafkaRequest =
            ChatRoomNotificationKafkaRequest.builder()
                .memberId(member.getId())
                .chatRoomId(request.getChatRoomId())
                .message(message)
                .url(url)
                .createdAt(LocalDateTime.now())
                .build();

        kafkaTemplate.send(KafkaTopic.NOTIFICATION, kafkaRequest);
      }
    }
  }

  private List<ChatParticipant> getChatParticipants(Long chatRoomId) {
    List<ChatParticipant> chatParticipants = chatParticipantRepository.
        findAllByChatRoom_Id(chatRoomId);
    log.info("채팅방 참여자 조회");
    return chatParticipants;
  }

  private Map<String, Object> getRedisCache(Long chatRoomId) {
    String visibleKey = RedisCacheType.VISIBLE_CACHE.getDescription()
        + chatRoomId;
    log.info("채팅방 유저 상태 정보 조회 : {}", chatRoomId);

    return redisService.getHashByKey(visibleKey);
  }

  @Override
  public NotificationProducerType getType() {
    return NotificationProducerType.CHATROOM_USER;
  }

  // 알림 메세지 반환
  private String createChatRoomMessage(String nickname, MessageType messageType) {
    StringBuilder sb = new StringBuilder(nickname);
    if (messageType.equals(ENTER)) {
      sb.append(ENTER_CHATROOM.getDescription());
    } else if (messageType.equals(EXIT)) {
      sb.append(EXIT_CHATROOM.getDescription());
    } else if (messageType.equals(KICK)) {
      sb.append(KICK_CHATROOM.getDescription());
    } else {
      throw new CustomException(INVALID_PARAMETER);
    }
    return sb.toString();
  }

  // URL 생성
  private String createChatRoomUrl(Long chatRoomId) {
    return NotificationType.CHATROOM.getDescription() + "/" + chatRoomId + "/enter";
  }

  private Member withdrawMember() {
    return Member.builder()
        .username("탈퇴한 회원")
        .build();
  }
}

