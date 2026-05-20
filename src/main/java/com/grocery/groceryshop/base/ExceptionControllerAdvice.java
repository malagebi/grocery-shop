package com.grocery.groceryshop.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

    /** 业务异常：自定义错误码 + 消息，HTTP 401 */
    @ExceptionHandler(CustomerException.class)
    public ResponseEntity<CommonResult<Void>> customerExceptionHandler(final CustomerException e) {
        log.warn("业务异常: code={}, message={}", e.getCode(), e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(CommonResult.error(e.getCode(), e.getMessage()));
    }

    /** 表单参数校验失败（@ModelAttribute + @Valid） */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<CommonResult<Void>> bindExceptionHandler(final BindException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.warn("参数绑定校验失败: {}", message);
        return ResponseEntity.badRequest().body(CommonResult.error(message));
    }

    /** 请求体参数校验失败（@RequestBody + @Valid） */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResult<Void>> methodArgumentNotValidHandler(final MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("请求体校验失败: {}", message);
        return ResponseEntity.badRequest().body(CommonResult.error(message));
    }

    /** 方法级参数校验失败（@Validated + @NotNull 等作用于 path/query 参数） */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CommonResult<Void>> constraintViolationHandler(final ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
        log.warn("参数约束校验失败: {}", message);
        return ResponseEntity.badRequest().body(CommonResult.error(message));
    }

    /** 必填请求参数缺失（@RequestParam required=true） */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<CommonResult<Void>> missingParamHandler(final MissingServletRequestParameterException ex) {
        String message = "缺少必填参数: " + ex.getParameterName();
        log.warn(message);
        return ResponseEntity.badRequest().body(CommonResult.error(message));
    }

    /** 参数类型不匹配（如字符串传给 Long 类型） */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CommonResult<Void>> typeMismatchHandler(final MethodArgumentTypeMismatchException ex) {
        String message = "参数类型错误: " + ex.getName() + " 应为 " + ex.getRequiredType().getSimpleName();
        log.warn(message);
        return ResponseEntity.badRequest().body(CommonResult.error(message));
    }

    /** 404：接口不存在（需配置 spring.mvc.throw-exception-if-no-handler-found=true） */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<CommonResult<Void>> noHandlerFoundHandler(final NoHandlerFoundException ex) {
        log.warn("接口不存在: {} {}", ex.getHttpMethod(), ex.getRequestURL());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(CommonResult.error("404", "接口不存在: " + ex.getRequestURL()));
    }

    /** 405：HTTP 方法不支持 */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<CommonResult<Void>> methodNotSupportedHandler(final HttpRequestMethodNotSupportedException ex) {
        log.warn("HTTP 方法不支持: {}", ex.getMethod());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(CommonResult.error("405", "不支持的请求方法: " + ex.getMethod()));
    }

    /** 415：Content-Type 不支持 */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<CommonResult<Void>> mediaTypeNotSupportedHandler(final HttpMediaTypeNotSupportedException ex) {
        log.warn("Content-Type 不支持: {}", ex.getContentType());
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(CommonResult.error("415", "不支持的 Content-Type: " + ex.getContentType()));
    }

    /** 兜底：未预期的运行时异常 */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CommonResult<Void>> runtimeExceptionHandler(final RuntimeException e) {
        log.error("未预期的运行时异常", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResult.error("500", "服务器内部错误，请稍后重试"));
    }

    /** 兜底：未预期的受检异常 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResult<Void>> exceptionHandler(final Exception e) {
        log.error("未预期的异常", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResult.error("500", "服务器内部错误，请稍后重试"));
    }
}
