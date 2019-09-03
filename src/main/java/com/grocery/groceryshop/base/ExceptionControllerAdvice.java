package com.grocery.groceryshop.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {
  @ExceptionHandler(BindException.class)
  public CommonResult BindExceptionHandler(final BindException ex, HttpServletResponse response) {
    log.info("进入BindException 异常");
    response.setStatus(HttpStatus.BAD_REQUEST.value());
    StringBuilder stb = new StringBuilder();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      stb.append("[");
      stb.append(error.getField());
      stb.append(":");
      stb.append(error.getDefaultMessage());
      stb.append("];");
    }
    return CommonResult.error(stb.toString());
  }

  @ExceptionHandler(RuntimeException.class)
  public CommonResult runtimeExceptionHandler(
      final RuntimeException e, HttpServletResponse response) {
    log.info("进入 RuntimeException 异常");
    response.setStatus(HttpStatus.BAD_REQUEST.value());
    return CommonResult.error(e.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public CommonResult exceptionHandler(final Exception e, HttpServletResponse response) {
    log.info("进入  Exception 异常");
    if (e instanceof NoHandlerFoundException) {
      response.setStatus(HttpStatus.NOT_FOUND.value());
      NoHandlerFoundException exception = (NoHandlerFoundException) e;
      return CommonResult.error(exception.getMessage());
    }

    response.setStatus(HttpStatus.BAD_REQUEST.value());
    return CommonResult.error(e.getMessage());
  }

  @ExceptionHandler(CustomerException.class)
  public CommonResult CustomerExceptionHandler(
      final CustomerException e, HttpServletResponse response) {
    log.info("进入 CustomerException 异常");
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    return CommonResult.error(e.getMessage());
  }
}
