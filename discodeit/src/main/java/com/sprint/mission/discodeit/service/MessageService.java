package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.service.dto.MessageUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    Message create(MessageCreateRequest request);

    Message find(UUID messageId);

    List<Message> findAllByChannelId(UUID channelId);

    Message update(MessageUpdateRequest request);

    void delete(UUID messageId);
}