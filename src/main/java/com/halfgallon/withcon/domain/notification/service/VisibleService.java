package com.halfgallon.withcon.domain.notification.service;

import com.halfgallon.withcon.domain.notification.dto.VisibleRequest;

public interface VisibleService {
  void createVisibleCache(Long memberId, VisibleRequest request);
}
