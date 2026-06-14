package org.example.service;

import org.example.AbstractIntegrationTest;
import org.example.dto.*;
import org.example.model.ShortUrl;
import org.example.model.User;
import org.example.repository.ShortUrlRepository;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class UrlCrudServiceTest extends AbstractIntegrationTest {

    @Autowired
    private UrlCrudService urlCrudService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    private User createUser() {
        return userRepository.save(User.builder()
                .username("testUser")
                .password("pass")
                .build());
    }

    private ShortUrl createUrl(User user, String shortCode, LocalDateTime expiresAt) {
        return shortUrlRepository.save(ShortUrl.builder()
                .originalUrl("https://google.com")
                .shortCode(shortCode)
                .clickCount(0L)
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiresAt(expiresAt)
                .build());
    }

    @Test
    void shouldCreateShortUrlWhenRequestIsValid() {

        User user = createUser();

        CreateShortUrlRequestDto request = new CreateShortUrlRequestDto();
        request.setOriginalUrl("https://google.com");
        request.setExpirationDays(5);

        UrlResponseDto result = urlCrudService.create(request, user.getUsername());

        assertNotNull(result);
        assertEquals("https://google.com", result.getOriginalUrl());
        assertNotNull(result.getShortCode());
        assertEquals(1, shortUrlRepository.count());
    }

    @Test
    void shouldReturnAllUserUrls() {

        User user = createUser();

        createUrl(user, "abc123", LocalDateTime.now().plusDays(1));

        var result = urlCrudService.getAllUserUrls(user.getUsername());

        assertEquals(1, result.size());
    }

    @Test
    void shouldGetByShortCodeWhenOwner() {

        User user = createUser();

        ShortUrl url = createUrl(user, "abc123", LocalDateTime.now().plusDays(1));

        UrlResponseDto result =
                urlCrudService.getByShortCode(url.getShortCode(), user.getUsername());

        assertEquals("https://google.com", result.getOriginalUrl());
    }

    @Test
    void shouldUpdateUrlWhenUserIsOwner() {

        User user = createUser();

        ShortUrl url = createUrl(user, "abc123", LocalDateTime.now().plusDays(1));

        UpdateShortUrlRequestDto request = new UpdateShortUrlRequestDto();
        request.setOriginalUrl("new");

        UrlResponseDto result =
                urlCrudService.update(url.getId(), request, user.getUsername());

        assertEquals("new", result.getOriginalUrl());
    }

    @Test
    void shouldPatchUrlWhenPartialDataIsProvided() {

        User user = createUser();

        ShortUrl url = createUrl(user, "abc123", LocalDateTime.now().plusDays(1));

        PatchShortUrlRequestDto request = new PatchShortUrlRequestDto();
        request.setOriginalUrl("patched");

        UrlResponseDto result =
                urlCrudService.patch(url.getId(), request, user.getUsername());

        assertEquals("patched", result.getOriginalUrl());
    }

    @Test
    void shouldDeleteUrlWhenOwner() {

        User user = createUser();

        ShortUrl url = createUrl(user, "abc123", LocalDateTime.now().plusDays(1));

        urlCrudService.delete(url.getId(), user.getUsername());

        assertTrue(shortUrlRepository.findById(url.getId()).isEmpty());
    }

    @Test
    void shouldRedirectAndIncrementClickCount() {

        User user = createUser();

        createUrl(user, "abc123", LocalDateTime.now().plusDays(1));

        String result = urlCrudService.redirect("abc123");

        assertEquals("https://google.com", result);
    }

    @Test
    void shouldThrowWhenRedirectExpired() {

        createUrl(
                createUser(),
                "abc123",
                LocalDateTime.now().minusDays(1)
        );

        assertThrows(RuntimeException.class,
                () -> urlCrudService.redirect("abc123"));
    }

    @Test
    void shouldThrowWhenRedirectNotFound() {

        assertThrows(RuntimeException.class,
                () -> urlCrudService.redirect("missing"));
    }
}