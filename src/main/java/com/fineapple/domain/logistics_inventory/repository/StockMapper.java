package com.fineapple.domain.logistics_inventory.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

@Mapper
public interface StockMapper {

    Integer selectStockByProductId(Long productId);

    void decreaseStock(@Param("productId") Long productId,
                       @Param("quantity") Integer quantity);
}