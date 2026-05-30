package com.grocery.groceryshop.config;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class FastJsonConfiguration {

  @Bean
  public HttpMessageConverters fastJsonHttpMessageConverters() {
    FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
    FastJsonConfig fastJsonConfig = new FastJsonConfig();
    fastJsonConfig.setWriterFeatures(
        // 是否格式化返回Json
        JSONWriter.Feature.PrettyFormat);
    // 处理中文乱码问题
    List<MediaType> fastMediaTypes = new ArrayList<>();
    fastMediaTypes.add(MediaType.APPLICATION_JSON);
    //fastMediaTypes.add(MediaType.TEXT_HTML);
    fastConverter.setSupportedMediaTypes(fastMediaTypes);
    fastConverter.setFastJsonConfig(fastJsonConfig);
    return new HttpMessageConverters(fastConverter);
  }
}
