package com.fineapple.Infrastructure.common;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommonCodeService {

    private final CommonCodeRepository commonCodeRepository;

    public CommonCodeService(CommonCodeRepository commonCodeRepository) {
        this.commonCodeRepository = commonCodeRepository;
    }

    @Cacheable("commonCodes")
    public List<CommonCode> getAllCommonCodes() {
        return commonCodeRepository.findAll();
    }


    @Cacheable(value = "commonCodes", key = "#name")
    public CommonCode getCommonCode(String name) {
        return commonCodeRepository.findByCodeName(name);
    }
}
