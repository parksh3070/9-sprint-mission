package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.dto.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.UserUpdateRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    public UserController(UserService userService, UserStatusService userStatusService) {
        this.userService = userService;
        this.userStatusService = userStatusService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public User create(@RequestBody UserCreateRequest request) {
        return userService.create(request);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<User> findAll() {
        return userService.findAll();
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public User update(@PathVariable UUID userId,
                       @RequestBody UserUpdateRequest request) {

        UserUpdateRequest newRequest = new UserUpdateRequest(
                userId,
                request.getNewUsername(),
                request.getNewEmail(),
                request.getNewPassword(),
                request.getProfileImageRequest()
        );

        return userService.update(newRequest);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable UUID userId) {
        userService.delete(userId);
    }

    // ✅ 온라인 상태 업데이트 (토글 방식)
    @RequestMapping(value = "/{userId}/online", method = RequestMethod.PATCH)
    public UserStatus toggleOnline(@PathVariable UUID userId) {
        return userStatusService.updateByUserId(userId);
    }
}