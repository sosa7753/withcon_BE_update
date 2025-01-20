package com.halfgallon.withcon.domain.notification.repository;

import com.halfgallon.withcon.domain.notification.entity.Notification;
import java.util.List;

public interface JdbcNotificationRepository {
  void saveAll(List<Notification> notifications);
}
