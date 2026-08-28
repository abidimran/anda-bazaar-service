package com.andabazaar.service;

import java.util.List;

import com.andabazaar.dto.news.NewsRequestDto;
import com.andabazaar.dto.news.NewsResponseDto;

public interface NewsService {

    NewsResponseDto createNews(
            NewsRequestDto request);

    NewsResponseDto updateNews(
            Long id,
            NewsRequestDto request);

    NewsResponseDto getNewsById(
            Long id);

    List<NewsResponseDto> getAllNews();

    List<NewsResponseDto> getPublishedNews();

    List<NewsResponseDto> getActiveNews();

    void publishNews(Long id);

    void unpublishNews(Long id);

    void deactivateNews(Long id);

    void deleteNews(Long id);
}