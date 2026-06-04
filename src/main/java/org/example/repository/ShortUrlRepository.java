package org.example.repository;

import org.example.model.ShortUrl;
import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, UUID> {

    List<ShortUrl> findAllByUserUsername(String username);

    Optional<ShortUrl> findByShortCode(String shortCode);
}
