package com.sprint.mission.discodeit.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class MessageCreateRequest {

    private final String content;
    private final UUID channelId;
    private final UUID authorId;

    // 첨부파일 없으면 null 또는 빈 리스트
    private final List<BinaryContentCreateRequest> attachments;
}