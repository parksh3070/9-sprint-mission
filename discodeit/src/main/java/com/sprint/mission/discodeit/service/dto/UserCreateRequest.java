package com.sprint.mission.discodeit.service.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserCreateRequest {
    private final String username;
    private final String email;
    private final String password;
    private final ProfileImageRequest profileImageRequest;

}
