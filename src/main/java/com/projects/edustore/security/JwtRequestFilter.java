package com.projects.edustore.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final CustomUserDetailService userDetailService;
    private final JwtUtil jwtUtil;

    public JwtRequestFilter(CustomUserDetailService userDetailService, JwtUtil jwtUtil) {
        this.userDetailService = userDetailService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        String userName = null;
        String jwt = null;

        //get JWT from header
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {  //if value of authorizationHeader exists and starts with 'Bearer'
            jwt = authorizationHeader.substring(7); //starting from first index after 'Bearer '
            userName = jwtUtil.extractUsername(jwt);
        }
        //if username is not null and not already authenticated
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailService.loadUserByUsername(userName);

            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); //webInfo about user session
                SecurityContextHolder.getContext().setAuthentication(authToken); //user is logged in
            }
        }
        filterChain.doFilter(request, response); //continue to next filter in chain
    }
}
