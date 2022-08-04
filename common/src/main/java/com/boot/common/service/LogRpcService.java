package com.boot.common.service;

import com.boot.common.dto.LogDO;
import com.boot.common.intercepter.FeignIntercepter;
import com.boot.common.utils.R;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;

@Headers("Content-Type:application/json")
@FeignClient(name = "api-base",configuration = FeignIntercepter.class)
public interface LogRpcService {
    @Async
    @PostMapping("log/save")
    R save(LogDO logDo);
}
