package com.boot.common.intercepter;

import com.boot.common.constants.CommonConstants;
import com.boot.common.context.FilterContextHandler;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.coyote.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//服务间的认证
public class FeignIntercepter implements RequestInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);
    @Override
    public void apply(RequestTemplate requestTemplate) {
        logger.info("----fegin设置token"+Thread.currentThread().getId());
        requestTemplate.header(CommonConstants.CONTEXT_TOKEN, FilterContextHandler.getToken());
    }
}
