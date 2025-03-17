package com.fineapple.domain.user.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Cart {
    private Long cartId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal totalPrice;
    private Long userId;
    private Long guestId;

    @Builder
    public Cart(Long cartId, LocalDateTime createdAt, LocalDateTime updatedAt, BigDecimal totalPrice, Long userId, Long guestId) {
        this.cartId = cartId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.totalPrice = totalPrice;
        this.userId = userId;
        this.guestId = guestId;
    }
}

