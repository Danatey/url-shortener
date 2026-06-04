package org.example.service;

import org.example.model.User;

public interface JwtService {
    String generateToken(User user);

    String extractUsername(String token);

    boolean isTokenValid(String token, User user);
}
