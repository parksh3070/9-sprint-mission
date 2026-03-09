package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.dto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.UserStatusUpdateRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserStatus create(UserStatusCreateRequest request) {
        UUID userId = request.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        if (userStatusRepository.findByUserId(userId).isPresent()) {
            throw new IllegalArgumentException("UserStatus already exists for userId=" + userId);
        }

        UserStatus userStatus = new UserStatus(user);
        user.assignStatus(userStatus);

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
    @Transactional
    public UserStatus update(UserStatusUpdateRequest request) {
        UUID id = request.getId();

        UserStatus userStatus = userStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("UserStatus with id " + id + " not found"));

        userStatus.updateLastSeenTime();
        return userStatus;
    }

    @Override
    @Transactional
    public UserStatus updateByUserId(UUID userId) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("UserStatus for userId " + userId + " not found"));

        userStatus.updateLastSeenTime();
        return userStatus;
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!userStatusRepository.existsById(id)) {
            throw new NoSuchElementException("UserStatus with id " + id + " not found");
        }

        userStatusRepository.deleteById(id);
    }
}