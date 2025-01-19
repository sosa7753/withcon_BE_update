package com.halfgallon.withcon.domain.notification.service.redis.service;

import java.util.List;

public interface RedisStringService {

  String get(String key);

  List<String> multiGet(List<Long> keys);

  void save(String key, String value);

  void delete(String key);

}
