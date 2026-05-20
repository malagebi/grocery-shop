package com.grocery.groceryshop.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

// 不加 @Configuration，避免被全局扫描注册为 Spring Bean；
// 仅作为 @FeignClient(configuration = FeignConfig.class) 的局部配置使用
public class FeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
