package com.example.urlshortner.repository;

import com.example.urlshortner.entity.Url;
import com.example.urlshortner.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByShortCode(String shortCode);
    boolean existsByShortCode(String shortCode);
    List<Url> findByExpiryDateBeforeAndIsActiveTrue(LocalDateTime time);

    @Modifying
    @Transactional
    @Query("UPDATE Url u SET u.clickCount = u.clickCount + 1 WHERE u.shortCode = :shortCode")
    void incrementClickCount(String shortCode);

    Page<Url> findAllByUser(User user, Pageable pageable);
}
