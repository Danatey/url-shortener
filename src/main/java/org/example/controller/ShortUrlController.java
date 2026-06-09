package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.config.ApiPaths;
import org.example.dto.CreateShortUrlRequestDto;
import org.example.dto.UrlResponseDto;
import org.example.service.UrlCrudService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ApiPaths.API_V1 + "/short-url")
@RequiredArgsConstructor
public class ShortUrlController {

    private final UrlCrudService urlCrudService;

    @Operation(summary = "Create short URL")
    @PostMapping
    public UrlResponseDto create(@Valid @RequestBody CreateShortUrlRequestDto request) {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return urlCrudService.create(request, username);
    }

    @Operation(summary = "Get all short URLs")
    @GetMapping
    public List<UrlResponseDto> getAllUserUrls() {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return urlCrudService.getAllUserUrls(username);
    }

    @Operation(summary = "Get short URL")
    @GetMapping("/{shortCode}")
    public UrlResponseDto getByShortCode(@PathVariable String shortCode) {
        return urlCrudService.getByShortCode(shortCode);
    }

    @Operation(summary = "Delete URL")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        urlCrudService.delete(id, username);
    }

    @GetMapping("/test-auth")
    public String test() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }
}
