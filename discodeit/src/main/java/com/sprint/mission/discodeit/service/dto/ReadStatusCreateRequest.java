package com.sprint.mission.discodeit.service.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ReadStatusCreateRequest {

    private final UUID userId;
    private final UUID channelId;
}