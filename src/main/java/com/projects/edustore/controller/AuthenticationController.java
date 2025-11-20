package com.projects.edustore.controller;
import com.projects.edustore.dto.AuthRequestDto;
import com.projects.edustore.dto.AuthResponseDto;
import com.projects.edustore.exception.AuthenticationFailedException;
import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.model.User;
import com.projects.edustore.repository.UserRepository;
import com.projects.edustore.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
    public ResponseEntity<Object> authenticated(Principal principal) {

        String username = principal.getName();
        User user = repos.findByUserName(username).orElseThrow(() -> new ResourceNotFoundException());
        Long id = user.getId();
        return ResponseEntity.ok().body("Username: " + username + " | Id: " + id);
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<AuthResponseDto> createAuthenticationToken(@Valid @RequestBody AuthRequestDto dto) {

        // check valid input format
        if (dto.getUserName() == null || dto.getPassword() == null) {
            throw new HttpMessageNotReadableException(
                    "Wrong JSON for login request. Example: { \"userName\": \"johndoe\", \"password\": \"wachtwoord123\" }"
            );
        }

        try {
            // check username and password validity
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUserName(), dto.getPassword()) //(principal, credentials)
            );

            // username from authentication object; .getName returns name of Principal.
            final String jwt = jwtUtil.generateToken(authentication.getName());

            // return jwt token
            return ResponseEntity.ok(new AuthResponseDto(jwt));

        } catch (AuthenticationException ex) { // all exceptions from authManager and authProviders
            throw new AuthenticationFailedException("Incorrect username or password");
        }
    }

}


