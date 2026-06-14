package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.config.ApiPaths;
import org.example.dto.*;
import org.example.service.UrlService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ApiPaths.API_V1 + "/short-url")
@RequiredArgsConstructor
public class ShortUrlController {

    private final UrlService urlService;

    @Operation(summary = "Create short URL")
    @PostMapping
    public UrlResponseDto create(@Valid @RequestBody CreateShortUrlRequestDto request) {

        String username = getUsername();
        return urlService.create(request, username);
    }

    @Operation(summary = "Get all user URLs")
    @GetMapping
    public List<UrlResponseDto> getAll() {

        String username = getUsername();
        return urlService.getAllUserUrls(username);
    }

    @Operation(summary = "Get by short code")
    @GetMapping("/{shortCode}")
    public UrlResponseDto getByShortCode(
            @PathVariable String shortCode,
            Authentication authentication
    ) {
        return urlService.getByShortCode(shortCode, authentication.getName());
    }

    @Operation(summary = "Update URL (PUT)")
    @PutMapping("/{id}")
    public UrlResponseDto update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateShortUrlRequestDto request
    ) {
        return urlService.update(id, request, getUsername());
    }

    @Operation(summary = "Patch URL (PATCH)")
    @PatchMapping("/{id}")
    public UrlResponseDto patch(
            @PathVariable UUID id,
            @RequestBody PatchShortUrlRequestDto request
    ) {
        return urlService.patch(id, request, getUsername());
    }

    @Operation(summary = "Delete URL")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        urlService.delete(id, getUsername());
    }

    @Operation(summary = "Get all active user URLs")
    @GetMapping("/active")
    public List<UrlResponseDto> getActive() {

        String username = getUsername();
        return urlService.getActiveUserUrls(username);
    }


    private String getUsername() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }
}