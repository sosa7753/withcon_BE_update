package com.halfgallon.withcon.domain.notification.kafka.producer.partitioner;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

@Slf4j
public class NotificationPartitioner implements Partitioner {

  @Override
  public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes,
      Cluster cluster) {

    int numPartitions = cluster.partitionsForTopic(topic).size();
    if(key == null) {
      return 0;
    }

    if(key instanceof Integer) {
      int intKey = (Integer)key;
      log.info("파티션 키: {}", key);
      return intKey % numPartitions;
    }
    log.info("예외 파티션 값");
    return 0;
  }

  @Override
  public void close() {

  }

  @Override
  public void configure(Map<String, ?> configs) {

  }
}
