package com.projects.edustore.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException(String message) {
        super(message);
    }

    @Component
    public static class CustomJwtAuthEntryPoint implements AuthenticationEntryPoint {


        @Override
        public void commence(HttpServletRequest request,
                             HttpServletResponse response,
                             AuthenticationException ex) throws IOException {

            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("text/plain");

           response.getWriter().write("Access denied: token is invalid or expired.");
        }
    }
}
