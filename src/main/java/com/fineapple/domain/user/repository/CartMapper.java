package com.fineapple.domain.user.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CartMapper {

    void deleteCartByUserId(Long id);
}
