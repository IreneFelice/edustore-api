package com.projects.edustore.controller;
import com.projects.edustore.dto.user.AuthRequestDto;
import com.projects.edustore.dto.user.AuthResponseDto;
import com.projects.edustore.dto.user.AuthenticatedResponseDto;
import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.mapper.AuthenticatedMapper;
import com.projects.edustore.model.User;
import com.projects.edustore.repository.UserRepository;
import com.projects.edustore.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;


@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository repos;

    public AuthenticationController(
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            UserRepository repos) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.repos = repos;
    }

    @GetMapping(value = "/authenticated")
    public ResponseEntity<AuthenticatedResponseDto> authenticated(Principal principal) {
        String username = principal.getName();
        User user = repos.findByUserName(username).orElseThrow(() -> new ResourceNotFoundException());

        return ResponseEntity.ok(AuthenticatedMapper.toResponse(user));
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<AuthResponseDto> createAuthenticationToken(@Valid @RequestBody AuthRequestDto dto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUserName(), dto.getPassword())
        );

        String jwt = jwtUtil.generateToken(authentication.getName());

        return ResponseEntity.ok(new AuthResponseDto(jwt));
    }

}


