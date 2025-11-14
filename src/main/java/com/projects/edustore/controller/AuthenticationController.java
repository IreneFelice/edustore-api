package com.projects.edustore.controller;
import com.projects.edustore.dto.AuthRequestDto;
import com.projects.edustore.dto.AuthResponseDto;
import com.projects.edustore.exception.AuthenticationFailedException;
import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.model.User;
import com.projects.edustore.repository.UserRepository;
import com.projects.edustore.security.CustomUserDetailService;
import com.projects.edustore.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;


@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailService customUserDetailService;
    private final JwtUtil jwtUtil;
    private final UserRepository repos;


    public AuthenticationController(
            AuthenticationManager authenticationManager,
            CustomUserDetailService customUserDetailService,
            JwtUtil jwtUtil,
            UserRepository repos) {
        this.authenticationManager = authenticationManager;
        this.customUserDetailService = customUserDetailService;
        this.jwtUtil = jwtUtil;
        this.repos = repos;
    }

    @GetMapping(value = "/authenticated")
    public ResponseEntity<Object> authenticated(Authentication authentication, Principal principal) {

        String username = principal.getName();
        User user = repos.findByUserName(username).orElseThrow(() -> new ResourceNotFoundException());
        var id = user.getId();
        return ResponseEntity.ok().body("Username: " + username + " | Id: " + id);
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<AuthResponseDto> createAuthenticationToken(@RequestBody AuthRequestDto dto) {

        if (dto.getUserName() == null || dto.getPassword() == null) {
            throw new HttpMessageNotReadableException(
                    "Wrong JSON for login request. Example: { \"userName\": \"johndoe\", \"password\": \"wachtwoord123\" }"
            );
        }

        String username = dto.getUserName();
        String password = dto.getPassword();

        boolean userExists = repos.findByUserName(username).isPresent();
        if (!userExists) {
            throw new AuthenticationFailedException("Incorrect username or password");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            final UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
            final String jwt = jwtUtil.generateToken(userDetails.getUsername());

            return ResponseEntity.ok(new AuthResponseDto(jwt));

        } catch (BadCredentialsException ex) {
            throw new AuthenticationFailedException("Incorrect username or password");
        }
    }

}


