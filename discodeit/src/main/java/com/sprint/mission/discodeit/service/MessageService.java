package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.response.MessageResponse;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;

import java.util.UUID;

public interface MessageService {

    Message create(MessageCreateRequest request);

    Message find(UUID messageId);

    PageResponse<MessageResponse> findMessages(UUID channelId, int page);

    Message update(MessageUpdateRequest request);

    void delete(UUID messageId);
}