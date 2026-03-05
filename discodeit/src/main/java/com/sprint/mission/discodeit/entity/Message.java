package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    private String content;
    private UUID channelId;
    private UUID authorId;

    // 첨부파일 id 목록
    private List<UUID> attachmentIds;

    public Message(String content, UUID channelId, UUID authorId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.content = content;
        this.channelId = channelId;
        this.authorId = authorId;

        // null 방지: 기본은 빈 리스트
        this.attachmentIds = new ArrayList<>();
    }

    // ✅ setter 대신: "첨부파일을 설정한다"는 도메인 행위 메서드
    public void attachFiles(List<UUID> attachmentIds) {
        if (attachmentIds == null) {
            this.attachmentIds = new ArrayList<>();
        } else {
            this.attachmentIds = new ArrayList<>(attachmentIds);
        }
        this.updatedAt = Instant.now();
    }

    public void update(String newContent) {
        boolean anyValueUpdated = false;

        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAt = Instant.now();
        }
    }
}