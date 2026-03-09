package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    @Transactional
    public ReadStatus create(ReadStatusCreateRequest request) {
        UUID userId = request.getUserId();
        UUID channelId = request.getChannelId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));

        boolean alreadyExists = readStatusRepository.findByUserIdAndChannelId(userId, channelId).isPresent();
        if (alreadyExists) {
            throw new IllegalArgumentException(
                    "ReadStatus already exists for userId=" + userId + ", channelId=" + channelId
            );
        }

        ReadStatus readStatus = new ReadStatus(user, channel);
        return readStatusRepository.save(readStatus);
    }

    @Override
    public ReadStatus find(UUID id) {
        return readStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("ReadStatus with id " + id + " not found"));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public ReadStatus update(ReadStatusUpdateRequest request) {
        UUID id = request.getId();

        ReadStatus readStatus = readStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("ReadStatus with id " + id + " not found"));

        readStatus.updateLastReadTime();
        return readStatus;
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!readStatusRepository.existsById(id)) {
            throw new NoSuchElementException("ReadStatus with id " + id + " not found");
        }
        readStatusRepository.deleteById(id);
    }
}