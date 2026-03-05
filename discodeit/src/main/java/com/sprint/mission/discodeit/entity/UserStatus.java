package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import java.time.Duration;

@Getter
public class UserStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID userId;
    private Instant lastSeenAt;

    public UserStatus(UUID userId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.userId = userId;
        this.lastSeenAt = Instant.now();
    }

    public void updateLastSeenTime() {
        this.lastSeenAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public boolean isOnline(){
        return Duration.between(this.lastSeenAt, Instant.now()).toMinutes() < 5;
    }
}