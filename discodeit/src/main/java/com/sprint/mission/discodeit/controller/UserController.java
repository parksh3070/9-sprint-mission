package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserResponse;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponse create(@RequestBody UserCreateRequest request) {
        return userService.create(request);
    }

    @GetMapping
    public List<UserResponse> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public UserResponse find(@PathVariable UUID userId) {
        return userService.find(userId);
    }

    @PutMapping("/{userId}")
    public UserResponse update(@PathVariable UUID userId,
                               @RequestBody UserUpdateRequest request) {
        return userService.update(userId, request);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable UUID userId) {
        userService.delete(userId);
    }
}