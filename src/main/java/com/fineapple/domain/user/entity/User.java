package com.fineapple.domain.user.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
public class User {
    private Long userId;
    private String email;
    private String password;
    private LocalDateTime createDate;
    private LocalDateTime updatedAt;
    private Boolean isActive;

    @Builder
    public User(Long userId, String email, String password, LocalDateTime createDate, LocalDateTime updatedAt, Boolean isActive) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.createDate = createDate;
        this.updatedAt = updatedAt;
        this.isActive = isActive;
    }
}

