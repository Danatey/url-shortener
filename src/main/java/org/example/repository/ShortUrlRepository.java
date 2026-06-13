package org.example.repository;

import org.example.model.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, UUID> {

    List<ShortUrl> findAllByUserUsername(String username);

    Optional<ShortUrl> findByShortCode(String shortCode);

    @Modifying
    @Query("""
       update ShortUrl s
       set s.clickCount = s.clickCount + 1
       where s.shortCode = :shortCode
       """)
    int incrementClickCount(@Param("shortCode") String shortCode);
}
