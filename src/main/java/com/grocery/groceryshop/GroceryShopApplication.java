package com.grocery.groceryshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@ServletComponentScan
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class GroceryShopApplication {

  public static void main(String[] args) {
    SpringApplication.run(GroceryShopApplication.class, args);
  }
}
