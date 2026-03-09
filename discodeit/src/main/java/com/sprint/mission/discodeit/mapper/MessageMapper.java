package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.MessageResponse;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public MessageResponse toResponse(Message message) {
        List<UUID> attachmentIds = message.getAttachments().stream()
                .map(attachment -> attachment.getId())
                .toList();

        return new MessageResponse(
                message.getId(),
                message.getContent(),
                message.getChannel().getId(),
                message.getAuthor().getId(),
                attachmentIds,
                message.getCreatedAt(),
                message.getUpdatedAt()
        );
    }
}