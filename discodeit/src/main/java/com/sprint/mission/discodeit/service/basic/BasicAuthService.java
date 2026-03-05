package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.dto.LoginRequest;
import lombok.RequiredArgsConstructor;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;

    @Override
    public User login(LoginRequest request) {
        User user = userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(request.getUsername()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("username 또는 password가 올바르지 않습니다."));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new NoSuchElementException("username 또는 password가 올바르지 않습니다.");
        }

        return user;
    }
}