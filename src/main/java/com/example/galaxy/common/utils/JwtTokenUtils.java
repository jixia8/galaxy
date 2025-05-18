package com.example.galaxy.common.utils;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

public interface JwtTokenUtils {
    String generateToken(UserDetails userDetails);

    String getUsernameFromToken(String token);

    Boolean isTokenExpired(String token) throws Exception;

    String refreshToken(String token);

    Boolean validateToken(String token, UserDetails userDetails) throws Exception;
}
