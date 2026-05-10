package com.jerryMouse.interceptor;

import com.jerryMouse.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String uri = request.getRequestURI();
        if (uri.contains("/login") || uri.contains("/uploads/")) {
            log.info("pass through request: {}", uri);
            return true;
        }

        String token = request.getHeader("token");
        if (token == null || token.isBlank()) {
            log.info("token is empty");
            response.setStatus(401);
            return false;
        }

        if (!JwtUtils.validateJwt(token)) {
            log.info("token is invalid");
            response.setStatus(401);
            return false;
        }

        return true;
    }
}
