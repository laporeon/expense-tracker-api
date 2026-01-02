package com.laporeon.expensetracker.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laporeon.expensetracker.dtos.response.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.warn("Authentication failed for path '{}'", request.getRequestURI());
        writeResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing token");
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.warn("Access denied for path '{}'", request.getRequestURI());
        writeResponse(response, HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource");
    }

    private void writeResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-store");

        objectMapper.writeValue(response.getWriter(), new ErrorResponseDTO(
                status,
                message,
                Instant.now()
        ));
    }
}
