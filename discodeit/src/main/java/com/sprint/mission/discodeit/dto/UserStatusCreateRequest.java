package com.sprint.mission.discodeit.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class UserStatusCreateRequest {
    private final UUID userId;
}