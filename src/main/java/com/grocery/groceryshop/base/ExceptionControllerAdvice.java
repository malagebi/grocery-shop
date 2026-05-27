package com.grocery.groceryshop.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.server.ResponseStatusException;
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

    /** 400：请求体 JSON 格式错误或无法解析 */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonResult<Void>> messageNotReadableHandler(final HttpMessageNotReadableException ex) {
        log.warn("请求体解析失败: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(CommonResult.error("请求体格式错误，请检查 JSON 是否合法"));
    }

    /** 406：客户端 Accept 头与服务端响应类型不匹配 */
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<CommonResult<Void>> mediaTypeNotAcceptableHandler(final HttpMediaTypeNotAcceptableException ex) {
        log.warn("响应类型不可接受: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(CommonResult.error("406", "服务端无法生成客户端 Accept 头所要求的响应类型"));
    }

    /** 400：缺少必填请求头 */
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<CommonResult<Void>> missingRequestHeaderHandler(final MissingRequestHeaderException ex) {
        String message = "缺少必填请求头: " + ex.getHeaderName();
        log.warn(message);
        return ResponseEntity.badRequest().body(CommonResult.error(message));
    }

    /** 400：路径变量缺失 */
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<CommonResult<Void>> missingPathVariableHandler(final MissingPathVariableException ex) {
        String message = "路径变量缺失: " + ex.getVariableName();
        log.warn(message);
        return ResponseEntity.badRequest().body(CommonResult.error(message));
    }

    /** 413：上传文件超过大小限制 */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<CommonResult<Void>> maxUploadSizeExceededHandler(final MaxUploadSizeExceededException ex) {
        log.warn("上传文件超限: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(CommonResult.error("413", "上传文件超过大小限制"));
    }

    /** 400：Multipart 请求解析失败 */
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<CommonResult<Void>> multipartExceptionHandler(final MultipartException ex) {
        log.warn("Multipart 请求解析失败: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(CommonResult.error("文件上传请求解析失败"));
    }

    /** 400：Multipart 请求中某个 Part 缺失（如 file 字段未传） */
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<CommonResult<Void>> missingPartHandler(final MissingServletRequestPartException ex) {
        String message = "缺少必填文件/表单项: " + ex.getRequestPartName();
        log.warn(message);
        return ResponseEntity.badRequest().body(CommonResult.error(message));
    }

    /** 409：数据库唯一键冲突 */
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<CommonResult<Void>> duplicateKeyHandler(final DuplicateKeyException ex) {
        log.warn("数据库唯一键冲突: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(CommonResult.error("409", "数据已存在，请勿重复提交"));
    }

    /** 500：数据库访问异常兜底（须在 DuplicateKeyException 之后声明） */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<CommonResult<Void>> dataAccessExceptionHandler(final DataAccessException ex) {
        log.error("数据库访问异常", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResult.error("500", "数据库操作失败，请稍后重试"));
    }

    /** 502：外部 HTTP 服务调用失败（RestTemplate） */
    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<CommonResult<Void>> restClientExceptionHandler(final RestClientException ex) {
        log.error("外部服务调用失败: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(CommonResult.error("502", "外部服务暂时不可用，请稍后重试"));
    }

    /** 503：异步请求处理超时 */
    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public ResponseEntity<CommonResult<Void>> asyncRequestTimeoutHandler(final AsyncRequestTimeoutException ex) {
        log.warn("异步请求超时: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(CommonResult.error("503", "请求处理超时，请稍后重试"));
    }

    /** 400：Service 层主动抛出的非法参数（IllegalArgumentException） */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonResult<Void>> illegalArgumentHandler(final IllegalArgumentException ex) {
        log.warn("非法参数: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(CommonResult.error(ex.getMessage()));
    }

    /** 按异常携带的 HTTP 状态码返回（Spring MVC 主动抛出的 ResponseStatusException） */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<CommonResult<Void>> responseStatusHandler(final ResponseStatusException ex) {
        log.warn("ResponseStatusException: status={}, reason={}", ex.getStatus(), ex.getReason());
        return ResponseEntity.status(ex.getStatus())
                .body(CommonResult.error(String.valueOf(ex.getStatus().value()), ex.getReason()));
    }

    /** 兜底：所有未被捕获的异常（含受检与非受检） */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResult<Void>> exceptionHandler(final Exception e) {
        log.error("未预期的异常", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResult.error("500", "服务器内部错误，请稍后重试"));
    }
}
