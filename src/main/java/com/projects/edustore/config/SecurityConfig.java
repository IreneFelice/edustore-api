package com.projects.edustore.config;

import com.projects.edustore.exception.AuthenticationFailedException;
import com.projects.edustore.exception.CustomAccessDeniedHandler;
import com.projects.edustore.security.CustomUserDetailService;
import com.projects.edustore.security.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    public final CustomUserDetailService customUserDetailService;
    private final JwtRequestFilter jwtRequestFilter;
    private final AuthenticationFailedException.CustomJwtAuthEntryPoint jwtAuthEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    public SecurityConfig(CustomUserDetailService customUserDetailService, JwtRequestFilter jwtRequestFilter, AuthenticationFailedException.CustomJwtAuthEntryPoint jwtAuthEntryPoint, CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.customUserDetailService = customUserDetailService;
        this.jwtRequestFilter = jwtRequestFilter;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(customUserDetailService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager(); // uses authenticationProvider
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // do not ask for csrf token
                .httpBasic(AbstractHttpConfigurer::disable) // do not use basic auth
                .cors(Customizer.withDefaults()) // use CorsConfig
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthEntryPoint) // not allowed by personal rights
                        .accessDeniedHandler(customAccessDeniedHandler)) // no valid token
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/authenticate", "/customers/register","/students/register").permitAll()
                        .requestMatchers("/authenticated").hasAnyRole("STUDENT", "CUSTOMER", "ADMIN")

                        .requestMatchers("/students/**").hasAnyRole("STUDENT", "ADMIN")
                        .requestMatchers("/customers/**").hasAnyRole("CUSTOMER")
                        .requestMatchers("/orders/customer/**").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers("/orders/student/**").hasAnyRole("STUDENT", "ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN")

                        .anyRequest().denyAll());

        return http.build();
    }
}
