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

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * Web 层日志切面：统一拦截 Controller 入口，记录请求参数、返回结果、耗时与异常。
 *
 * <p>切点覆盖 {@code com.grocery.groceryshop} 包下任意 {@code controller} 子包，
 * 以及任意以 {@code Controller} 结尾的类的 public 方法。
 */
@Slf4j
@Aspect
@Component
public class WebLogAspect {

  /**
   * 切点定义：匹配所有 Controller 的 public 方法。
   *
   * <p>两个表达式用 {@code ||} 合并，分别覆盖：
   * <ul>
   *   <li>放在 {@code *.controller.*} 包下的控制器（主模块、trainticket 子模块）</li>
   *   <li>类名以 {@code Controller} 结尾、但未放在标准包下的控制器（兼容历史命名）</li>
   * </ul>
   * 方法体留空，仅作为切点签名供 {@link #around(ProceedingJoinPoint)} 引用。
   */
  @Pointcut(
      "execution(public * com.grocery.groceryshop..controller..*.*(..))"
          + " || execution(public * com.grocery.groceryshop..*Controller.*(..))")
  public void webLog() {}

  /**
   * 环绕通知：在目标方法前后打印请求/响应日志，并统计耗时。
   *
   * <p>流程：
   * <ol>
   *   <li>从 {@link RequestContextHolder} 取出当前 HTTP 请求（无 HTTP 上下文时降级为 {@code non-http}）</li>
   *   <li>打印 {@code ==>} 入站日志：HTTP 方法、URL、目标类.方法、序列化后的入参</li>
   *   <li>调用 {@link ProceedingJoinPoint#proceed()} 执行目标方法</li>
   *   <li>正常返回：打印 {@code <==} 出站日志，含耗时与返回值</li>
   *   <li>抛出异常：打印异常摘要后**继续向上抛**，交由 {@code ExceptionControllerAdvice} 兜底</li>
   * </ol>
   *
   * @param joinPoint AOP 连接点，承载目标方法签名与入参
   * @return 目标方法的原始返回值
   * @throws Throwable 透传目标方法抛出的任何异常
   */
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

  /**
   * 安全地将方法入参转换为可记录的字符串。
   *
   * <p>对常见的不可序列化或会引发递归的对象做替换：
   * <ul>
   *   <li>{@link MultipartFile}：仅保留原始文件名，避免读取文件流</li>
   *   <li>{@link javax.servlet.ServletRequest} / {@link javax.servlet.ServletResponse}：
   *       仅记录类名，避免 FastJSON 反射触发循环引用或拉起整个 Servlet 容器对象图</li>
   *   <li>其他对象：交由 FastJSON 序列化</li>
   * </ul>
   *
   * @param args 目标方法的原始参数数组
   * @return JSON 字符串；参数为空时返回 {@code "[]"}
   */
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
                  if (a instanceof jakarta.servlet.ServletRequest
                      || a instanceof jakarta.servlet.ServletResponse) {
                    return a.getClass().getSimpleName();
                  }
                  return a;
                })
            .toArray();
    return safeJson(filtered);
  }

  /**
   * 安全地将任意对象序列化为 JSON，序列化失败时回退到 {@link Object#toString()}。
   *
   * <p>用于规避如下场景导致的整条请求链路打不出日志的问题：
   * 含 {@code Stream}、{@code InputStream}、循环引用、或自定义 {@code getter} 抛异常的对象。
   *
   * @param obj 待序列化对象，允许为 {@code null}
   * @return JSON 字符串；{@code null} 时返回字面量 {@code "null"}
   */
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
