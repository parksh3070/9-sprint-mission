package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.service.dto.ReadStatusUpdateRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatus create(ReadStatusCreateRequest request) {
        UUID userId = request.getUserId();
        UUID channelId = request.getChannelId();

        // 1) 관련 User/Channel 존재 검증
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }

        // 2) 중복 검증: 같은 userId+channelId 조합이 이미 존재하면 안 됨
        boolean alreadyExists = readStatusRepository.findByUserIdAndChannelId(userId, channelId).isPresent();
        if (alreadyExists) {
            throw new IllegalArgumentException("ReadStatus already exists for userId=" + userId + ", channelId=" + channelId);
        }

        // 3) 생성 + 저장
        ReadStatus readStatus = new ReadStatus(userId, channelId);
        return readStatusRepository.save(readStatus);
    }

    @Override
    public ReadStatus find(UUID id) {
        return readStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("ReadStatus with id " + id + " not found"));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        // Repository에 이 메서드가 아직 없으므로, findAll로 필터링(임시 구현)
        return readStatusRepository.findAll().stream()
                .filter(rs -> rs.getUserId().equals(userId))
                .toList();
    }

    @Override
    public ReadStatus update(ReadStatusUpdateRequest request) {
        UUID id = request.getId();

        ReadStatus readStatus = readStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("ReadStatus with id " + id + " not found"));

        readStatus.updateLastReadTime();
        return readStatusRepository.save(readStatus);
    }

    @Override
    public void delete(UUID id) {
        // 존재 안 하면 예외
        if (readStatusRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException("ReadStatus with id " + id + " not found");
        }
        readStatusRepository.deleteById(id);
    }
}