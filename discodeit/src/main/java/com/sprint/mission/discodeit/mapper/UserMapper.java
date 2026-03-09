package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.dto.UserResponse;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        Boolean online = Optional.ofNullable(user.getStatus())
                .map(UserStatus::isOnline)
                .orElse(false);

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getProfile() != null ? user.getProfile().getId() : null,
                online
        );
    }
}