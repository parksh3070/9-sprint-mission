package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.dto.ChannelCreatePrivateRequest;
import com.sprint.mission.discodeit.service.dto.ChannelCreatePublicRequest;
import com.sprint.mission.discodeit.service.dto.ChannelUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel createPublic(ChannelCreatePublicRequest request);

    Channel createPrivate(ChannelCreatePrivateRequest request);

    Channel find(UUID channelId);

    List<Channel> findAllByUserId(UUID userId);

    Channel update(ChannelUpdateRequest request);

    void delete(UUID channelId);
}