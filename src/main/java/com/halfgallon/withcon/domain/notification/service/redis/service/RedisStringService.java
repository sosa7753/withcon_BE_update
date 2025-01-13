package com.halfgallon.withcon.domain.notification.service.redis.service;

public interface RedisStringService {

  String get(String key);

  void save(String key, String value);

  void delete(String key);

}
