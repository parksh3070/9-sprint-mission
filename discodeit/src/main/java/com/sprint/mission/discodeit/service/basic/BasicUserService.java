package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserResponse;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse create(UserCreateRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 username입니다.");
        }

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                null
        );

        User savedUser = userRepository.save(user);

        UserStatus status = new UserStatus(savedUser);
        savedUser.assignStatus(status);
        userStatusRepository.save(status);

        return userMapper.toResponse(savedUser);
    }

    @Override
    public UserResponse find(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new NoSuchElementException("User not found: " + userId));

        return userMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> findAll() {

        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public UserResponse update(UUID userId, UserUpdateRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new NoSuchElementException("User not found: " + userId));

        user.update(
                request.getNewUsername(),
                request.getNewEmail(),
                request.getNewPassword(),
                null
        );

        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public void delete(UUID userId) {

        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User not found: " + userId);
        }

        userRepository.deleteById(userId);
    }
}