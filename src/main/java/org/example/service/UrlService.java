package org.example.service;

import org.example.dto.CreateShortUrlRequestDto;
import org.example.dto.UrlResponseDto;

import java.util.List;
import java.util.UUID;

public interface UrlService {

    UrlResponseDto create(CreateShortUrlRequestDto request, String username);

    List<UrlResponseDto> getAllUserUrls(String username);

    UrlResponseDto getByShortCode(String shortCode);

    void delete(UUID id, String username);

    String redirect(String shortCode);
}
