package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.dto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.service.dto.UserStatusUpdateRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatus create(UserStatusCreateRequest request) {
        UUID userId = request.getUserId();

        // User 존재 검증
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }

        // 중복 방지: 같은 userId에 대한 status가 이미 있으면 안 됨
        if (userStatusRepository.findByUserId(userId).isPresent()) {
            throw new IllegalArgumentException("UserStatus already exists for userId=" + userId);
        }

        UserStatus userStatus = new UserStatus(userId);
        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus find(UUID id) {
        return userStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("UserStatus with id " + id + " not found"));
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Override
    public UserStatus update(UserStatusUpdateRequest request) {
        UUID id = request.getId();
        UserStatus userStatus = find(id);

        userStatus.updateLastSeenTime();
        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus updateByUserId(UUID userId) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("UserStatus for userId " + userId + " not found"));

        userStatus.updateLastSeenTime();
        return userStatusRepository.save(userStatus);
    }

    @Override
    public void delete(UUID id) {
        // 없으면 예외
        if (userStatusRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException("UserStatus with id " + id + " not found");
        }
        userStatusRepository.deleteById(id);
    }
}