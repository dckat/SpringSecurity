package com.meerkat.ss.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Logger;

public class AuthenticationLoggingFilter implements Filter {

    private final Logger logger = Logger.getLogger(
            AuthenticationLoggingFilter.class.getName());

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain filterChain) throws IOException, ServletException {

        var httpRequest = (HttpServletRequest) request;

        var requestId = httpRequest.getHeader("Request-Id");

        logger.info("인증 성공 아이디 " + requestId);  // 요청 성공시 ID값 기록

        filterChain.doFilter(request, response);
    }
}
