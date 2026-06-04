package org.example.service;

import org.example.dto.AuthResponseDto;
import org.example.dto.LoginRequestDto;
import org.example.dto.RegisterRequestDto;

public interface AuthService {
    void register(RegisterRequestDto request);

    AuthResponseDto login(LoginRequestDto request);
}
