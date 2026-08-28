package com.andabazaar.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "news",
    indexes = {
        @Index(name = "idx_news_published", columnList = "published"),
        @Index(name = "idx_news_created_at", columnList = "created_at")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 500)
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 500)
    private String imageUrl;

    @Column(length = 500)
    private String sourceUrl;

    @Column(length = 100)
    private String sourceName;

    @Builder.Default
    @Column(nullable = false)
    private Boolean published = false;

    private LocalDateTime publishedAt;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (published == null) {
            published = false;
        }

        if (active == null) {
            active = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }
}