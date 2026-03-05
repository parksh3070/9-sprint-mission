package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserQueryController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    public UserQueryController(UserService userService, UserStatusService userStatusService) {
        this.userService = userService;
        this.userStatusService = userStatusService;
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public ResponseEntity<List<UserDto>> findAll() {
        List<User> users = userService.findAll();

        // userStatusService가 findAll()을 제공하니까 그걸로 온라인 매핑
        var statuses = userStatusService.findAll();

        List<UserDto> result = users.stream()
                .map(u -> {
                    boolean online = statuses.stream()
                            .filter(s -> s.getUserId().equals(u.getId()))
                            .findFirst()
                            .map(UserStatus::isOnline)   // ⚠️ UserStatus에 isOnline() 있어야 함
                            .orElse(false);

                    return new UserDto(
                            u.getId(),
                            u.getCreatedAt(),
                            u.getUpdatedAt(),
                            u.getUsername(),
                            u.getEmail(),
                            u.getProfileImageId(),
                            online
                    );
                })
                .toList();

        return ResponseEntity.ok(result);
    }
}