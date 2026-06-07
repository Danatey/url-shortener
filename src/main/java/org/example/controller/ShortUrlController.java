package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.CreateShortUrlRequestDto;
import org.example.dto.UrlResponseDto;
import org.example.service.UrlCrudService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/short-url")
@RequiredArgsConstructor
public class ShortUrlController {

    private final UrlCrudService urlCrudService;

    @PostMapping
    public UrlResponseDto create(@RequestBody CreateShortUrlRequestDto request) {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return urlCrudService.create(request, username);
    }

    @GetMapping
    public List<UrlResponseDto> getAllUserUrls() {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return urlCrudService.getAllUserUrls(username);
    }

    @GetMapping("/{shortCode}")
    public UrlResponseDto getByShortCode(@PathVariable String shortCode) {
        return urlCrudService.getByShortCode(shortCode);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        urlCrudService.delete(id, username);
    }

    @GetMapping("/r/{shortCode}")
    public String redirect(@PathVariable String shortCode) {
        return urlCrudService.redirect(shortCode);
    }
}
