package com.andabazaar.mapper;

import org.springframework.stereotype.Component;

import com.andabazaar.dto.news.NewsResponseDto;
import com.andabazaar.entity.News;

@Component
public class NewsMapper {

    public NewsResponseDto toDto(News news) {

        if (news == null) {
            return null;
        }

        return NewsResponseDto.builder()
                .id(news.getId())
                .title(news.getTitle())
                .summary(news.getSummary())
                .content(news.getContent())
                .imageUrl(news.getImageUrl())
                .sourceUrl(news.getSourceUrl())
                .sourceName(news.getSourceName())
                .published(news.getPublished())
                .publishedAt(news.getPublishedAt())
                .active(news.getActive())
                .createdAt(news.getCreatedAt())
                .updatedAt(news.getUpdatedAt())
                .build();
    }
}