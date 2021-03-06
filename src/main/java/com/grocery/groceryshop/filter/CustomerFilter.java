package com.grocery.groceryshop.filter;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "customerFilter", urlPatterns = "/*")
@Slf4j
public class CustomerFilter implements Filter {
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
    log.info("customerFilter  doFilter ................");
    filterChain.doFilter(request, servletResponse);
  }

  @Override
  public void destroy() {
    log.info("customerFilter  destroy ................");
  }
}
