package com.projects.edustore.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

    @Component
    public class CustomAuthEntryPoint implements AuthenticationEntryPoint {


        @Override
        public void commence(HttpServletRequest request,
                             HttpServletResponse response,
                             AuthenticationException ex) throws IOException {

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("text/plain");

            response.getWriter().write("Token is invalid or expired.");
        }
    }

