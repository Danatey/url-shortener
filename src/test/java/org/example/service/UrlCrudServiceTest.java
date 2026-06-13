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

        assertNotNull(result);
        assertEquals("https://google.com", result.getOriginalUrl());
    }

    @Test
    void shouldUpdateUrlWhenUserIsOwner() {

        UUID id = UUID.randomUUID();

        User user = User.builder()
                .id(UUID.randomUUID())
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
                .id(UUID.randomUUID())
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
    void shouldThrowNotFoundExceptionWhenUrlDoesNotExist() {

        UUID id = UUID.randomUUID();

        when(shortUrlRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> urlCrudService.update(id,
                        new UpdateShortUrlRequestDto(),
                        "testUser"));
    }

    @Test
    void shouldThrowAccessDeniedExceptionWhenUserIsNotOwner() {

        UUID id = UUID.randomUUID();

        User owner = User.builder()
                .id(UUID.randomUUID())
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
                        "testUser"));
    }
}