package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.dto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.service.dto.UserStatusUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {

    UserStatus create(UserStatusCreateRequest request);

    UserStatus find(UUID id);

    List<UserStatus> findAll();

    UserStatus update(UserStatusUpdateRequest request);

    UserStatus updateByUserId(UUID userId);

    void delete(UUID id);
}