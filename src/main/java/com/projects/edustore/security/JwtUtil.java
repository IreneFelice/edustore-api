package com.projects.edustore.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;


@Service
public class JwtUtil {
    private static final String STRING_KEY = "supersecretkey1234567890123456ssshhh";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(STRING_KEY.getBytes());
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    private static final MacAlgorithm ALGORITHM = Jwts.SIG.HS256;

    // put data claims in payload of JWT String
    public String generateToken(String userName) {
        return Jwts.builder()
                .subject(userName)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(KEY, ALGORITHM) //signature
                .compact(); //encode to JWT string (header.payload.signature)
    }

    // Read and verify token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)// decode JWT, verify signature with key
                .getPayload();           // only after successful verification
    }


    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }


    public Boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUsername(token); //also key verification through extractAllClaims
        return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}
