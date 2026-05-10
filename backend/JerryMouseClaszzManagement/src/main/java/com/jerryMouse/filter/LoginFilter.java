package com.jerryMouse.filter;

import com.jerryMouse.utils.CurrentHolder;
import com.jerryMouse.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebFilter("/*")
public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String uri = request.getRequestURI();
        if (uri.contains("/login") || uri.contains("/uploads/")) {
            log.info("pass through request: {}", uri);
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("token");
        if (token == null || token.isBlank()) {
            log.info("token is empty");
            response.setStatus(401);
            return;
        }

        Boolean result = JwtUtils.validateJwt(token);
        if (!result) {
            log.info("token is invalid");
            response.setStatus(401);
            return;
        }

        Claims claims = JwtUtils.parseJwt(token);
        Integer id = (Integer) claims.get("id");
        CurrentHolder.setCurrentId(id);
        log.info("token is valid");

        try {
            filterChain.doFilter(request, response);
        } finally {
            CurrentHolder.remove();
        }
    }
}
