package com.system.fsharksocialmedia.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
    	response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");

        // Tạo thông điệp JSON
        String jsonResponse = String.format("{\"timestamp\": \"%s\", \"status\": %d, \"error\": \"Forbidden\", \"message\": \"%s\", \"path\": \"%s\"}",
                Instant.now(), HttpStatus.FORBIDDEN.value(), accessDeniedException.getMessage(), request.getRequestURI());

        // Ghi thông điệp JSON vào phản hồi
        PrintWriter writer = response.getWriter();
        writer.write(jsonResponse);
        writer.flush();
    }
}