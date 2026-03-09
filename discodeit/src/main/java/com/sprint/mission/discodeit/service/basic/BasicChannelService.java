package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.dto.ChannelCreatePrivateRequest;
import com.sprint.mission.discodeit.dto.ChannelCreatePublicRequest;
import com.sprint.mission.discodeit.dto.ChannelUpdateRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    @Transactional
    public Channel createPublic(ChannelCreatePublicRequest request) {
        Channel channel = new Channel(
                ChannelType.PUBLIC,
                request.getName(),
                request.getDescription()
        );
        return channelRepository.save(channel);
    }

    @Override
    @Transactional
    public Channel createPrivate(ChannelCreatePrivateRequest request) {
        List<UUID> participantIds = request.getParticipantUserIds();

        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        Channel savedChannel = channelRepository.save(channel);

        for (UUID userId : participantIds) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

            boolean alreadyExists = readStatusRepository
                    .findByUserIdAndChannelId(user.getId(), savedChannel.getId())
                    .isPresent();

            if (alreadyExists) {
                throw new IllegalArgumentException(
                        "ReadStatus already exists for userId=" + user.getId()
                                + ", channelId=" + savedChannel.getId()
                );
            }

            ReadStatus readStatus = new ReadStatus(user, savedChannel);
            readStatusRepository.save(readStatus);
        }

        return savedChannel;
    }

    @Override
    public Channel find(UUID channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
    }

    @Override
    public List<Channel> findAllByUserId(UUID userId) {
        List<Channel> publicChannels = channelRepository.findAll().stream()
                .filter(channel -> channel.getType() == ChannelType.PUBLIC)
                .toList();

        List<Channel> privateChannels = readStatusRepository.findByUserId(userId).stream()
                .map(ReadStatus::getChannel)
                .filter(channel -> channel.getType() == ChannelType.PRIVATE)
                .toList();

        return java.util.stream.Stream.concat(publicChannels.stream(), privateChannels.stream())
                .distinct()
                .toList();
    }

    @Override
    @Transactional
    public Channel update(ChannelUpdateRequest request) {
        Channel channel = channelRepository.findById(request.getChannelId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Channel with id " + request.getChannelId() + " not found"
                ));

        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalStateException("PRIVATE channel cannot be updated");
        }

        channel.update(request.getNewName(), request.getNewDescription());
        return channel;
    }

    @Override
    @Transactional
    public void delete(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));

        List<Message> messages = messageRepository.findByChannelId(channelId);
        for (Message message : messages) {
            messageRepository.delete(message);
        }

        List<ReadStatus> readStatuses = readStatusRepository.findByChannelId(channelId);
        for (ReadStatus readStatus : readStatuses) {
            readStatusRepository.delete(readStatus);
        }

        channelRepository.delete(channel);
    }
}