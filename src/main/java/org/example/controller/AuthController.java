package org.example.controller;

import org.example.dto.AuthResponseDto;
import org.example.dto.LoginRequestDto;
import org.example.service.AuthCrudService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthCrudService authCrudService;

    public AuthController(AuthCrudService authCrudService) {
        this.authCrudService = authCrudService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authCrudService.login(request));
    }
}
