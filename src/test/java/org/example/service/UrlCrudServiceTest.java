package org.example.service;

import org.example.dto.*;
import org.example.model.ShortUrl;
import org.example.model.User;
import org.example.repository.ShortUrlRepository;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlCrudServiceTest {

    @Mock
    private ShortUrlRepository shortUrlRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UrlCrudService urlCrudService;

    @Test
    void shouldCreateShortUrlWhenRequestIsValid() {

        User user = User.builder()
                .id(UUID.randomUUID())
                .username("testUser")
                .build();

        CreateShortUrlRequestDto request = new CreateShortUrlRequestDto();
        request.setOriginalUrl("https://google.com");
        request.setExpirationDays(5);

        when(userRepository.findByUsername("testUser"))
                .thenReturn(Optional.of(user));

        when(shortUrlRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        UrlResponseDto result = urlCrudService.create(request, "testUser");

        assertEquals("https://google.com", result.getOriginalUrl());
        assertNotNull(result.getShortCode());
    }

    @Test
    void shouldReturnAllUserUrls() {

        ShortUrl url = ShortUrl.builder()
                .id(UUID.randomUUID())
                .originalUrl("https://google.com")
                .shortCode("abc123")
                .clickCount(0L)
                .build();

        when(shortUrlRepository.findAllByUserUsername("testUser"))
                .thenReturn(List.of(url));

        var result = urlCrudService.getAllUserUrls("testUser");

        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnActiveUserUrls() {

        ShortUrl url = ShortUrl.builder()
                .originalUrl("https://google.com")
                .expiresAt(LocalDateTime.now().plusDays(1))
                .build();

        when(shortUrlRepository.findAllByUserUsername("testUser"))
                .thenReturn(List.of(url));

        var result = urlCrudService.getActiveUserUrls("testUser");

        assertEquals(1, result.size());
    }

    @Test
    void shouldGetByShortCodeWhenOwner() {

        User user = User.builder()
                .username("testUser")
                .build();

        ShortUrl url = ShortUrl.builder()
                .shortCode("abc123")
                .originalUrl("https://google.com")
                .user(user)
                .build();

        when(shortUrlRepository.findByShortCode("abc123"))
                .thenReturn(Optional.of(url));

        var result = urlCrudService.getByShortCode("abc123", "testUser");

        assertEquals("https://google.com", result.getOriginalUrl());
    }

    @Test
    void shouldThrowWhenShortCodeNotFound() {

        when(shortUrlRepository.findByShortCode("abc"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> urlCrudService.getByShortCode("abc", "testUser"));
    }

    @Test
    void shouldUpdateUrlWhenUserIsOwner() {

        UUID id = UUID.randomUUID();

        User user = User.builder()
                .username("testUser")
                .build();

        ShortUrl url = ShortUrl.builder()
                .id(id)
                .originalUrl("old")
                .user(user)
                .build();

        UpdateShortUrlRequestDto request = new UpdateShortUrlRequestDto();
        request.setOriginalUrl("new");

        when(shortUrlRepository.findById(id))
                .thenReturn(Optional.of(url));

        when(shortUrlRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        UrlResponseDto result = urlCrudService.update(id, request, "testUser");

        assertEquals("new", result.getOriginalUrl());
    }

    @Test
    void shouldPatchUrlWhenPartialDataIsProvided() {

        UUID id = UUID.randomUUID();

        User user = User.builder()
                .username("testUser")
                .build();

        ShortUrl url = ShortUrl.builder()
                .id(id)
                .originalUrl("old")
                .user(user)
                .build();

        PatchShortUrlRequestDto request = new PatchShortUrlRequestDto();
        request.setOriginalUrl("patched");

        when(shortUrlRepository.findById(id))
                .thenReturn(Optional.of(url));

        when(shortUrlRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        UrlResponseDto result = urlCrudService.patch(id, request, "testUser");

        assertEquals("patched", result.getOriginalUrl());
    }

    @Test
    void shouldDeleteUrlWhenOwner() {

        UUID id = UUID.randomUUID();

        User user = User.builder()
                .username("testUser")
                .build();

        ShortUrl url = ShortUrl.builder()
                .id(id)
                .user(user)
                .build();

        when(shortUrlRepository.findById(id))
                .thenReturn(Optional.of(url));

        urlCrudService.delete(id, "testUser");

        verify(shortUrlRepository).delete(url);
    }

    @Test
    void shouldRedirectAndIncrementClickCount() {

        ShortUrl url = ShortUrl.builder()
                .shortCode("abc123")
                .originalUrl("https://google.com")
                .clickCount(0L)
                .expiresAt(LocalDateTime.now().plusDays(1))
                .build();

        when(shortUrlRepository.findByShortCode("abc123"))
                .thenReturn(Optional.of(url));

        when(shortUrlRepository.incrementClickCount("abc123"))
                .thenReturn(1);

        String result = urlCrudService.redirect("abc123");

        assertEquals("https://google.com", result);

        verify(shortUrlRepository).incrementClickCount("abc123");
    }

    @Test
    void shouldThrowWhenRedirectExpired() {

        ShortUrl url = ShortUrl.builder()
                .shortCode("abc123")
                .expiresAt(LocalDateTime.now().minusDays(1))
                .build();

        when(shortUrlRepository.findByShortCode("abc123"))
                .thenReturn(Optional.of(url));

        assertThrows(ResponseStatusException.class,
                () -> urlCrudService.redirect("abc123"));
    }

    @Test
    void shouldThrowWhenRedirectNotFound() {

        when(shortUrlRepository.findByShortCode("abc123"))
                .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> urlCrudService.redirect("abc123"));
    }

    @Test
    void shouldThrowWhenNotOwnerOnUpdate() {

        UUID id = UUID.randomUUID();

        User owner = User.builder()
                .username("owner")
                .build();

        ShortUrl url = ShortUrl.builder()
                .id(id)
                .user(owner)
                .build();

        when(shortUrlRepository.findById(id))
                .thenReturn(Optional.of(url));

        assertThrows(RuntimeException.class,
                () -> urlCrudService.update(id,
                        new UpdateShortUrlRequestDto(),
                        "otherUser"));
    }
}