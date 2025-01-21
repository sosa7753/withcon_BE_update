package com.halfgallon.withcon.domain.notification.kafka.producer;

import com.halfgallon.withcon.domain.chat.entity.ChatParticipant;
import com.halfgallon.withcon.domain.chat.repository.ChatParticipantRepository;
import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.notification.constant.NotificationMessage;
import com.halfgallon.withcon.domain.notification.constant.NotificationType;
import com.halfgallon.withcon.domain.notification.constant.RedisCacheType;
import com.halfgallon.withcon.domain.notification.constant.VisibleType;
import com.halfgallon.withcon.domain.notification.dto.ChatMessageNotificationRequest;
import com.halfgallon.withcon.domain.notification.kafka.constant.KafkaTopic;
import com.halfgallon.withcon.domain.notification.kafka.constant.NotificationProducerType;
import com.halfgallon.withcon.domain.notification.kafka.dto.ChatRoomNotificationKafkaArrayRequest;
import com.halfgallon.withcon.domain.notification.service.redis.service.RedisHashService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMessageNotificationProducer implements Producer<ChatMessageNotificationRequest> {

  private static final int DIVVALUE = 100;
  private static final List<ArrayList<Long>> MEMBERS = new ArrayList<>();

  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final ChatParticipantRepository chatParticipantRepository;
  private final RedisHashService redisHashService;

  @Override
  public void send(ChatMessageNotificationRequest request) {
    List<ChatParticipant> chatParticipants = getChatParticipants(
        request.getChatRoomId());
    if(chatParticipants.isEmpty()) {
      return;
    }

    String visibleKey = RedisCacheType.VISIBLE_CACHE.getDescription()
        + request.getChatRoomId();
    Map<String, Object> cache = redisHashService.getHashByKey(visibleKey);
    if (cache == null) {
      return;
    }

    String message = createNewMessageNotification();
    String url = createNewMessageUrl(request.getChatRoomId());

    // 묶음 처리
    int group = (chatParticipants.size() + DIVVALUE - 1) / DIVVALUE;
    for(int i=0; i<group; i++) {
      MEMBERS.add(new ArrayList<>());
    }

    int row = 0;
    int col = 0;
    for (ChatParticipant chatParticipant : chatParticipants) {
      Member member = chatParticipant.getMember();

      Object visible = cache.get(String.valueOf(member.getId()));
      if (visible != null && (VisibleType.valueOf((String) visible) == VisibleType.HIDDEN)) {

        MEMBERS.get(row).add(member.getId());
        cache.put(String.valueOf(member.getId()), VisibleType.NONE);
        col++;

        // user group 경계선
        if (col % DIVVALUE == 0 || col == chatParticipants.size()) {
          ChatRoomNotificationKafkaArrayRequest kafkaArrayRequest
              = ChatRoomNotificationKafkaArrayRequest.builder()
              .members(MEMBERS.get(row))
              .message(message)
              .url(url)
              .createdAt(LocalDateTime.now())
              .build();
          row++;

          kafkaTemplate.send(KafkaTopic.NOTIFICATION,String.valueOf(row-1), kafkaArrayRequest);

        }
      }
    }
    redisHashService.saveToHash(visibleKey, cache, 24);
  }

  @Override
  public NotificationProducerType getType() {
    return NotificationProducerType.CHATROOM_MESSAGE;
  }

  private List<ChatParticipant> getChatParticipants(Long chatRoomId) {
    List<ChatParticipant> chatParticipants = chatParticipantRepository.
        findAllByChatRoom_Id(chatRoomId);
    log.info("채팅방 참여자 조회");
    return chatParticipants;
  }

  private String createNewMessageNotification() {
    return NotificationMessage.NEW_MESSAGE_FROM_CHATROOM.getDescription();
  }

  private String createNewMessageUrl(Long chatRoomId) {
    return NotificationType.CHATROOM.getDescription() + "/" + chatRoomId + "/enter";
  }
}
