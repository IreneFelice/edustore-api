package com.projects.edustore.config;

import com.projects.edustore.security.CustomAccessDeniedHandler;
import com.projects.edustore.security.CustomAuthEntryPoint;
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
    private final CustomAuthEntryPoint authEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(CustomUserDetailService customUserDetailService, JwtRequestFilter jwtRequestFilter, CustomAuthEntryPoint authEntryPoint, CustomAccessDeniedHandler accessDeniedHandler) {
        this.customUserDetailService = customUserDetailService;
        this.jwtRequestFilter = jwtRequestFilter;
        this.authEntryPoint = authEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
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
                        .authenticationEntryPoint(authEntryPoint) // no valid token
                        .accessDeniedHandler(accessDeniedHandler)) // access by personal rights
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/authenticate", "/customers/register","/students/register").permitAll()
                        .requestMatchers("/authenticated").hasAnyRole("STUDENT", "CUSTOMER", "ADMIN")
                        .requestMatchers("/orders/**").hasAnyRole("CUSTOMER", "STUDENT", "ADMIN")
                        .requestMatchers("/students/**").hasAnyRole("STUDENT", "ADMIN")
                        .requestMatchers("/customers/**").hasAnyRole("CUSTOMER")

                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN")

                        .anyRequest().denyAll());

        return http.build();
    }
}
