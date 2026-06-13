package org.example.service;

import org.example.dto.CreateShortUrlRequestDto;
import org.example.dto.PatchShortUrlRequestDto;
import org.example.dto.UpdateShortUrlRequestDto;
import org.example.dto.UrlResponseDto;

import java.util.List;
import java.util.UUID;

public interface UrlService {

    UrlResponseDto create(CreateShortUrlRequestDto request, String username);

    List<UrlResponseDto> getAllUserUrls(String username);

    UrlResponseDto getByShortCode(String shortCode, String username);

    void delete(UUID id, String username);

    UrlResponseDto update(UUID id, UpdateShortUrlRequestDto request, String username);

    UrlResponseDto patch(UUID id, PatchShortUrlRequestDto request, String username);

    String redirect(String shortCode);
}
