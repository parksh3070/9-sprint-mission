package com.sprint.mission.discodeit.service.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class UserUpdateRequest {

    private final UUID userId;
    private final String newUsername;
    private final String newEmail;
    private final String newPassword;
    private final ProfileImageRequest profileImageRequest;
}