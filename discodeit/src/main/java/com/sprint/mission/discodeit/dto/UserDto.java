package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.util.UUID;

public record UserDto(
        UUID id,
        String username,
        String email,
        BinaryContentRepository profile,
        Boolean online
) {

}