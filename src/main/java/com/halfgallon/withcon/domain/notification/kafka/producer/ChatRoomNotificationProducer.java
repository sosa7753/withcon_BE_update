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
import com.halfgallon.withcon.domain.notification.dto.ChatRoomNotificationRequest;
import com.halfgallon.withcon.domain.notification.kafka.constant.KafkaTopic;
import com.halfgallon.withcon.domain.notification.kafka.dto.ChatRoomNotificationKafkaRequest;
import com.halfgallon.withcon.global.exception.CustomException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatRoomNotificationProducer implements Producer {

  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final ChatParticipantRepository chatParticipantRepository;
  private final MemberRepository memberRepository;

  @Override
  public void createProducer(Object request) {
    ChatRoomNotificationRequest req = (ChatRoomNotificationRequest) request;

    List<ChatParticipant> chatParticipants = chatParticipantRepository.
        findAllByChatRoom_Id(req.getChatRoomId());
    log.info("채팅방 참여자 조회");

    Member target = memberRepository.findById(req.getTargetId())
        .orElse(withdrawMember());

    String message = createChatRoomMessage(target.getNickname(), req.getMessageType());
    String url = createChatRoomUrl(req.getChatRoomId());

    for (ChatParticipant chatParticipant : chatParticipants) {
      Member member = chatParticipant.getMember();
      if (member.getId().equals(req.getTargetId())) {
        log.info("target 제외 : {}", req.getTargetId());
        continue;
      }

      ChatRoomNotificationKafkaRequest kafkaRequest =
          ChatRoomNotificationKafkaRequest.builder()
              .memberId(member.getId())
              .chatRoomId(req.getChatRoomId())
              .message(message)
              .url(url)
              .createdAt(LocalDateTime.now())
              .build();

      kafkaTemplate.send(KafkaTopic.NOTIFICATION, kafkaRequest);
    }
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

