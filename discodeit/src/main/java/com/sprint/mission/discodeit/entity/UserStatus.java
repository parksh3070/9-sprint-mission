package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_status")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserStatus extends BaseUpdatableEntity {

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private Instant lastSeenAt;

    public UserStatus(User user) {
        this.user = user;
        this.lastSeenAt = Instant.now();
    }

    public void updateLastSeenTime() {
        this.lastSeenAt = Instant.now();
    }

    public boolean isOnline() {
        return lastSeenAt != null
                && Duration.between(lastSeenAt, Instant.now()).toMinutes() < 5;
    }

    public void assignUser(User user) {
        this.user = user;
    }
}