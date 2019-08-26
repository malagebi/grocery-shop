package com.grocery.groceryshop.config;

import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.annotation.MapperScan;

@Configuration
@MapperScan("com.*.mapper")
public class datasourceConfig {
}
