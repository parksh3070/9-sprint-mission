package com.sprint.mission.discodeit.service.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class BinaryContentCreateRequest {

    private final byte[] data;
    private final String filename;
    private final String contentType;

    // 누가 소유자인지 (프로필이면 userId, 첨부파일이면 author or owner)
    private final UUID ownerUserId;

    // 메시지 첨부면 messageId가 있고, 프로필이면 null
    private final UUID messageId;
}