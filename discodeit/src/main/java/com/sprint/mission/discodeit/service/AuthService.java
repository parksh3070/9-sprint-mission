package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.dto.LoginRequest;

public interface AuthService {

    User login(LoginRequest request);
}