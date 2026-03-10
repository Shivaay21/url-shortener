package com.example.urlshortner.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Table(name = "urls")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String longUrl;
    private String shortCode;
    private String customAlias;
    private LocalDateTime createdAt;
    private LocalDateTime expiryDate;
    private int clickCount;
    private boolean isActive;
}
