package com.grocery.groceryshop.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = {"com.grocery.groceryshop.mapper"})
public class datasourceConfig {
}
