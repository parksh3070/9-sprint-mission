package com.sprint.mission.discodeit.service.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ChannelUpdateRequest {
    private final UUID channelId;
    private final String newName;
    private final String newDescription;
}