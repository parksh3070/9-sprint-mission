package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.dto.LoginRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 로그인
    // POST /api/auth/login
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public User login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}