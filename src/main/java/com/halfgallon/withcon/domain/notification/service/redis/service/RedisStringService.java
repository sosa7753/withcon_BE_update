package com.halfgallon.withcon.domain.notification.service.redis.service;

public interface RedisStringService {

  String get(String key);

  void save(String Key, String value);

  void delete(String Key);

}
