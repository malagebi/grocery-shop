package com.grocery.groceryshop.config;

import com.grocery.groceryshop.Interceptor.CustomerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry
        .addInterceptor(new CustomerInterceptor())
        .addPathPatterns("/**")
        .excludePathPatterns("/swagger-ui/**")
        .excludePathPatterns("/swagger-ui.html")
        .excludePathPatterns("/v3/api-docs/**")
        .excludePathPatterns("/swagger-resources/**")
        .excludePathPatterns("/error")
        .excludePathPatterns("/webjars/**");
  }
}
