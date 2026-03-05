package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.dto.ChannelCreatePrivateRequest;
import com.sprint.mission.discodeit.service.dto.ChannelCreatePublicRequest;
import com.sprint.mission.discodeit.service.dto.ChannelUpdateRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    public Channel createPublic(ChannelCreatePublicRequest request) {
        Channel channel = new Channel(ChannelType.PUBLIC, request.getName(), request.getDescription());
        return channelRepository.save(channel);
    }

    @Override
    public Channel createPrivate(ChannelCreatePrivateRequest request) {
        List<UUID> participantIds = request.getParticipantUserIds();

        // 1) 참여자 유저 존재 검증
        for (UUID userId : participantIds) {
            if (!userRepository.existsById(userId)) {
                throw new NoSuchElementException("User with id " + userId + " not found");
            }
        }

        // 2) PRIVATE 채널 생성 (name/description 생략)
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        channelRepository.save(channel);

        // 3) 참여자별 ReadStatus 생성
        for (UUID userId : participantIds) {
            // 혹시 중복 생성 방지(같은 userId+channelId)
            if (readStatusRepository.findByUserIdAndChannelId(userId, channel.getId()).isPresent()) {
                throw new IllegalArgumentException("ReadStatus already exists for userId=" + userId + ", channelId=" + channel.getId());
            }
            readStatusRepository.save(new ReadStatus(userId, channel.getId()));
        }

        return channel;
    }

    @Override
    public Channel find(UUID channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
    }

    @Override
    public List<Channel> findAllByUserId(UUID userId) {
        // PUBLIC 채널은 전부 포함 + PRIVATE는 userId가 참여한 것만 포함
        return channelRepository.findAll().stream()
                .filter(ch -> {
                    if (ch.getType() == ChannelType.PUBLIC) return true;
                    // PRIVATE인 경우: ReadStatus가 있으면 참여자로 간주
                    return readStatusRepository.findByUserIdAndChannelId(userId, ch.getId()).isPresent();
                })
                .toList();
    }

    @Override
    public Channel update(ChannelUpdateRequest request) {
        UUID channelId = request.getChannelId();

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));

        // PRIVATE는 수정 불가
        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalStateException("PRIVATE channel cannot be updated");
        }

        channel.update(request.getNewName(), request.getNewDescription());
        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));

        // 1) 관련 Message 삭제 (MessageRepository에 조건 조회가 아직 없어서 findAll로 필터링)
        List<Message> messagesToDelete = messageRepository.findAll().stream()
                .filter(m -> m.getChannelId().equals(channelId))
                .toList();
        for (Message m : messagesToDelete) {
            messageRepository.deleteById(m.getId());
        }

        // 2) 관련 ReadStatus 삭제 (ReadStatusRepository에 조건 조회가 아직 없어서 findAll로 필터링)
        List<ReadStatus> readStatusesToDelete = readStatusRepository.findAll().stream()
                .filter(rs -> rs.getChannelId().equals(channelId))
                .toList();
        for (ReadStatus rs : readStatusesToDelete) {
            readStatusRepository.deleteById(rs.getId());
        }

        // 3) 마지막에 채널 삭제
        channelRepository.deleteById(channel.getId());
    }
}