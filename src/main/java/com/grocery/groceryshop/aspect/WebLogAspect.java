package com.grocery.groceryshop.aspect;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@Aspect
@Component
public class WebLogAspect {

  @Pointcut(
      "execution(public * com.grocery.groceryshop..controller..*.*(..))"
          + " || execution(public * com.grocery.groceryshop..*Controller.*(..))")
  public void webLog() {}

  @Around("webLog()")
  public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();

    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    String classMethod = method.getDeclaringClass().getSimpleName() + "." + method.getName();

    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    HttpServletRequest request = attributes == null ? null : attributes.getRequest();
    String url = request == null ? "non-http" : request.getRequestURL().toString();
    String httpMethod = request == null ? "-" : request.getMethod();

    log.info(
        "==> {} {} | handler={} | args={}",
        httpMethod,
        url,
        classMethod,
        safeArgs(joinPoint.getArgs()));

    try {
      Object result = joinPoint.proceed();
      log.info(
          "<== {} {} | handler={} | cost={}ms | result={}",
          httpMethod,
          url,
          classMethod,
          System.currentTimeMillis() - start,
          safeJson(result));
      return result;
    } catch (Throwable ex) {
      log.error(
          "<== {} {} | handler={} | cost={}ms | exception={}",
          httpMethod,
          url,
          classMethod,
          System.currentTimeMillis() - start,
          ex.toString());
      throw ex;
    }
  }

  private String safeArgs(Object[] args) {
    if (args == null || args.length == 0) {
      return "[]";
    }
    Object[] filtered =
        Arrays.stream(args)
            .map(
                a -> {
                  if (a == null) return null;
                  if (a instanceof MultipartFile) {
                    return "MultipartFile(" + ((MultipartFile) a).getOriginalFilename() + ")";
                  }
                  if (a instanceof javax.servlet.ServletRequest
                      || a instanceof javax.servlet.ServletResponse) {
                    return a.getClass().getSimpleName();
                  }
                  return a;
                })
            .toArray();
    return safeJson(filtered);
  }

  private String safeJson(Object obj) {
    if (Objects.isNull(obj)) {
      return "null";
    }
    try {
      return JSON.toJSONString(obj);
    } catch (Exception e) {
      return obj.toString();
    }
  }
}
