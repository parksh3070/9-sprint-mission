package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.service.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.service.dto.MessageUpdateRequest;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public Message create(MessageCreateRequest request) {
        // 1) Channel/User 존재 검증
        if (!channelRepository.existsById(request.getChannelId())) {
            throw new NoSuchElementException("Channel with id " + request.getChannelId() + " not found");
        }
        if (!userRepository.existsById(request.getAuthorId())) {
            throw new NoSuchElementException("User with id " + request.getAuthorId() + " not found");
        }

        // 2) Message 생성 + 저장
        Message message = new Message(request.getContent(), request.getChannelId(), request.getAuthorId());

        // 3) 첨부파일 저장 후 id 목록 연결
        List<UUID> attachmentIds = new ArrayList<>();
        List<BinaryContentCreateRequest> attachments = request.getAttachments();
        if (attachments != null) {
            for (BinaryContentCreateRequest a : attachments) {
                BinaryContent binaryContent = new BinaryContent(
                        a.getData(),
                        a.getFilename(),
                        a.getContentType(),
                        request.getAuthorId(),
                        message.getId() // messageId 연결
                );
                binaryContentRepository.save(binaryContent);
                attachmentIds.add(binaryContent.getId());
            }
        }

        // ⚠️ Message 엔티티에 attachmentIds를 넣을 수 있어야 함
        message.attachFiles(attachmentIds);

        return messageRepository.save(message);
    }

    @Override
    public Message find(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.findAll().stream()
                .filter(m -> m.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public Message update(MessageUpdateRequest request) {
        Message message = find(request.getMessageId());
        message.update(request.getNewContent());
        return messageRepository.save(message);
    }

    @Override
    public void delete(UUID messageId) {
        Message message = find(messageId);

        // 1) 첨부파일 같이 삭제
        // ⚠️ Message에 attachmentIds가 있어야 함
        List<UUID> attachmentIds = message.getAttachmentIds();
        if (attachmentIds != null) {
            for (UUID id : attachmentIds) {
                binaryContentRepository.deleteById(id);
            }
        }

        // 2) 메시지 삭제
        messageRepository.deleteById(messageId);
    }
}