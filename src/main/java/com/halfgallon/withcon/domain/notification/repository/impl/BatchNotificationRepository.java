package com.halfgallon.withcon.domain.notification.repository.impl;

import com.halfgallon.withcon.domain.notification.entity.Notification;
import com.halfgallon.withcon.domain.notification.repository.JdbcNotificationRepository;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BatchNotificationRepository implements JdbcNotificationRepository {

  private final JdbcTemplate jdbcTemplate;

  public BatchNotificationRepository(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public void saveAll(List<Notification> notifications) {
    jdbcTemplate.batchUpdate(
        "INSERT INTO notification (message, url, notification_type, created_at, member_id, read_status) " +
            "VALUES (?, ?, ? ,? ,?, ?)",
        notifications,
        100,
        (PreparedStatement ps, Notification notification) -> {
          ps.setString(1, notification.getMessage());
          ps.setString(2, notification.getUrl());
          ps.setString(3, notification.getNotificationType().toString());
          ps.setTimestamp(4, Timestamp.valueOf(notification.getCreatedAt()));
          ps.setLong(5, notification.getMember().getId());
          ps.setBoolean(6, false);
        });
  }
}
