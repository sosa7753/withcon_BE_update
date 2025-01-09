package com.halfgallon.withcon.domain.notification.service.impl;

import com.halfgallon.withcon.domain.notification.kafka.constant.NotificationProducerType;
import com.halfgallon.withcon.domain.notification.kafka.producer.Producer;
import jakarta.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeneralNotificationService {

  private final List<Producer<?>> producers;
  private final EnumMap<NotificationProducerType, Producer<?>> producerMap = new EnumMap<>(NotificationProducerType.class);

  @PostConstruct
  public void init() {
    for(Producer<?> producer : producers) {
      producerMap.put(producer.getType(), producer);
    }
  }

  public <T> void produce(NotificationProducerType type, T request) {
    if(producerMap.get(type) == null) {
      log.info("Type에 맞는 producer 없음 : {}", type);
    }else {
      // 제네릭 캐스팅 컴파일 경고 무시
      @SuppressWarnings("unchecked")
      Producer<T> producer = (Producer<T>) producerMap.get(type);
      producer.send(request);
    }
  }
}
