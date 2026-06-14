package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.*;
import org.example.exception.UrlNotFoundException;
import org.example.model.ShortUrl;
import org.example.model.User;
import org.example.repository.ShortUrlRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UrlCrudService implements UrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final UserRepository userRepository;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    public UrlResponseDto create(CreateShortUrlRequestDto request, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
                );

        ShortUrl shortUrl = ShortUrl.builder()
                .originalUrl(request.getOriginalUrl())
                .shortCode(generateCode())
                .clickCount(0L)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(request.getExpirationDays()))
                .user(user)
                .build();

        return map(shortUrlRepository.save(shortUrl));
    }

    @Override
    public List<UrlResponseDto> getAllUserUrls(String username) {
        return shortUrlRepository.findAllByUserUsername(username)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public UrlResponseDto getByShortCode(String shortCode, String username) {

        ShortUrl url = shortUrlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("URL not found"));

        checkOwner(url, username);

        return map(url);
    }

    @Override
    public UrlResponseDto update(UUID id, UpdateShortUrlRequestDto request, String username) {

        ShortUrl url = getOwnedUrl(id, username);

        url.setOriginalUrl(request.getOriginalUrl());

        if (request.getExpiresAt() != null) {
            url.setExpiresAt(request.getExpiresAt());
        }

        return map(shortUrlRepository.save(url));
    }

    @Override
    public UrlResponseDto patch(UUID id, PatchShortUrlRequestDto request, String username) {

        ShortUrl url = getOwnedUrl(id, username);

        if (request.getOriginalUrl() != null) {
            url.setOriginalUrl(request.getOriginalUrl());
        }

        if (request.getExpiresAt() != null) {
            url.setExpiresAt(request.getExpiresAt());
        }

        return map(shortUrlRepository.save(url));
    }

    @Override
    public void delete(UUID id, String username) {

        ShortUrl url = getOwnedUrl(id, username);

        shortUrlRepository.delete(url);
    }

    @Transactional
    @Override
    public String redirect(String shortCode) {

        ShortUrl url = shortUrlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Short URL not found"
                ));

        if (url.getExpiresAt() != null &&
                url.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.GONE, "Link expired");
        }

        shortUrlRepository.incrementClickCount(shortCode);

        return url.getOriginalUrl();
    }

    @Override
    public List<UrlResponseDto> getActiveUserUrls(String username) {

        LocalDateTime now = LocalDateTime.now();

        return shortUrlRepository.findAllByUserUsername(username)
                .stream()
                .filter(url ->
                        url.getExpiresAt() == null ||
                                url.getExpiresAt().isAfter(now)
                )
                .map(this::map)
                .toList();
    }

    private ShortUrl getOwnedUrl(UUID id, String username) {
        ShortUrl url = shortUrlRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Short URL not found"
                ));

        checkOwner(url, username);
        return url;
    }

    private void checkOwner(ShortUrl url, String username) {
        if (!url.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("Access denied");
        }
    }

    private UrlResponseDto map(ShortUrl url) {
        return UrlResponseDto.builder()
                .id(url.getId())
                .originalUrl(url.getOriginalUrl())
                .shortCode(url.getShortCode())
                .shortUrl(baseUrl + "/r/" + url.getShortCode())
                .clickCount(url.getClickCount())
                .createdAt(url.getCreatedAt())
                .expiresAt(url.getExpiresAt())
                .build();
    }

    private String generateCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
}