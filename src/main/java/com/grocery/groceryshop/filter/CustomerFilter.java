package com.grocery.groceryshop.filter;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@WebFilter(filterName = "customerFilter", urlPatterns = "/*")
@Slf4j
public class CustomerFilter implements Filter {

  /** MDC 中存放 traceId 的 key，需与 logback-spring.xml 中的 %X{traceId} 保持一致。 */
  private static final String TRACE_ID = "traceId";

  /** 上游若已传入 traceId，则透传；否则在此处生成。 */
  private static final String TRACE_HEADER = "X-Trace-Id";

  @Override
  @SneakyThrows
  public void init(FilterConfig filterConfig) {
    log.info("customerFilter  init ................");
  }

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    String traceId = request.getHeader(TRACE_HEADER);
    if (traceId == null || traceId.isEmpty()) {
      traceId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
    MDC.put(TRACE_ID, traceId);
    try {
      log.info("customerFilter  doFilter ................");
      filterChain.doFilter(request, servletResponse);
    } finally {
      MDC.remove(TRACE_ID);
    }
  }

  @Override
  public void destroy() {
    log.info("customerFilter  destroy ................");
  }
}
