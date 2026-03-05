package com.sprint.mission.discodeit.service.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProfileImageRequest {
    private final byte[] data;
    private final String filename;
    private final String contentType;
}
