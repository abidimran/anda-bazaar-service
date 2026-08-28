package com.andabazaar.dto.news;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsResponseDto {

    private Long id;

    private String title;

    private String summary;

    private String content;

    private String imageUrl;

    private String sourceUrl;

    private String sourceName;

    private Boolean published;

    private LocalDateTime publishedAt;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}