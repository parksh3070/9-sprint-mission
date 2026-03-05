package com.sprint.mission.discodeit.service.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ChannelCreatePrivateRequest {
    private final List<UUID> participantUserIds;
}