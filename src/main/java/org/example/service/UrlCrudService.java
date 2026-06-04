package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.CreateShortUrlRequestDto;
import org.example.dto.UrlResponseDto;
import org.example.model.ShortUrl;
import org.example.model.User;
import org.example.repository.ShortUrlRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UrlCrudService implements UrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final UserRepository userRepository;

    @Override
    public UrlResponseDto create(CreateShortUrlRequestDto request,
                                 String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow();

        ShortUrl shortUrl = ShortUrl.builder()
                .originalUrl(request.getOriginalUrl())
                .shortCode(generateCode())
                .clickCount(0L)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now()
                        .plusDays(request.getExpirationDays()))
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
    public UrlResponseDto getByShortCode(String shortCode) {

        return shortUrlRepository.findByShortCode(shortCode)
                .map(this::map)
                .orElseThrow();
    }

    @Override
    public void delete(UUID id, String username) {

        ShortUrl url = shortUrlRepository.findById(id)
                .orElseThrow();

        if (!url.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Access denied");
        }

        shortUrlRepository.delete(url);
    }

    @Override
    public String redirect(String shortCode) {

        ShortUrl url = shortUrlRepository.findByShortCode(shortCode)
                .orElseThrow();

        url.setClickCount(url.getClickCount() + 1);

        shortUrlRepository.save(url);

        return url.getOriginalUrl();
    }

    private UrlResponseDto map(ShortUrl url) {

        return UrlResponseDto.builder()
                .id(url.getId())
                .originalUrl(url.getOriginalUrl())
                .shortCode(url.getShortCode())
                .clickCount(url.getClickCount())
                .createdAt(url.getCreatedAt())
                .expiresAt(url.getExpiresAt())
                .build();
    }

    private String generateCode() {

        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8);
    }
}
