package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.dto.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.UserUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(UserCreateRequest request);
    User find(UUID userId);
    List<User> findAll();
    User update(UserUpdateRequest request);
    void delete(UUID userId);
}
