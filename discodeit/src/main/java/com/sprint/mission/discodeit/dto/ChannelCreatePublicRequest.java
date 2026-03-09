package com.sprint.mission.discodeit.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChannelCreatePublicRequest {
    private final String name;
    private final String description;
}