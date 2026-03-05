package com.sprint.mission.discodeit.entity;


import lombok.Getter;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private Instant createdAt; // 파일 업로드 기록
    private byte[] data;
    private String filename;
    private String contentType;
    private UUID ownerUserId;
    private UUID messageId;

    public BinaryContent(byte[] data,
                         String filename,
                         String contentType,
                         UUID ownerUserId,
                         UUID messageId) {
        this.id = UUID.randomUUID();// 고유식별자
        this.createdAt = Instant.now();// 생성시간 = 현재시간
        this.data = data;
        this.filename = filename;
        this.contentType = contentType;
        this.ownerUserId = ownerUserId;
        this.messageId = messageId;

    }

    public byte[] getBytes() {
        return this.data;
    }

}
