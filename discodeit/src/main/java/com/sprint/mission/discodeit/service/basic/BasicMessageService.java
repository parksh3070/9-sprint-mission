package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.response.MessageResponse;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;
    private final PageMapper pageMapper;

    @Override
    @Transactional
    public Message create(MessageCreateRequest request) {
        Channel channel = channelRepository.findById(request.getChannelId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Channel with id " + request.getChannelId() + " not found"
                ));

        User author = userRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new NoSuchElementException(
                        "User with id " + request.getAuthorId() + " not found"
                ));

        Message message = new Message(
                request.getContent(),
                channel,
                author
        );

        List<BinaryContentCreateRequest> attachments = request.getAttachments();
        if (attachments != null) {
            for (BinaryContentCreateRequest attachmentRequest : attachments) {
                BinaryContent file = new BinaryContent(
                        attachmentRequest.getFilename(),
                        (long) attachmentRequest.getData().length,
                        attachmentRequest.getContentType()
                );
                message.addAttachment(file);
            }
        }

        return messageRepository.save(message);
    }

    @Override
    public Message find(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Message with id " + messageId + " not found"
                ));
    }

    @Override
    public PageResponse<MessageResponse> findMessages(UUID channelId, int page) {
        Pageable pageable = PageRequest.of(page, 50);

        Slice<Message> slice = messageRepository.findByChannelIdOrderByCreatedAtDesc(channelId, pageable);

        return pageMapper.toResponse(slice, messageMapper::toResponse);
    }

    @Override
    @Transactional
    public Message update(MessageUpdateRequest request) {
        Message message = messageRepository.findById(request.getMessageId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Message with id " + request.getMessageId() + " not found"
                ));

        message.update(request.getNewContent());
        return message;
    }

    @Override
    @Transactional
    public void delete(UUID messageId) {
        if (!messageRepository.existsById(messageId)) {
            throw new NoSuchElementException(
                    "Message with id " + messageId + " not found"
            );
        }

        messageRepository.deleteById(messageId);
    }
}