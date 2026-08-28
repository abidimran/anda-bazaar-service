package com.andabazaar.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andabazaar.dto.news.NewsRequestDto;
import com.andabazaar.dto.news.NewsResponseDto;
import com.andabazaar.entity.News;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.repository.NewsRepository;
import com.andabazaar.service.NewsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    @Override
    public NewsResponseDto createNews(
            NewsRequestDto request) {

        boolean published =
                Boolean.TRUE.equals(request.getPublished());

        News news = News.builder()
                .title(request.getTitle().trim())
                .summary(request.getSummary())
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .sourceUrl(request.getSourceUrl())
                .sourceName(request.getSourceName())
                .published(published)
                .publishedAt(
                        published
                                ? LocalDateTime.now()
                                : null
                )
                .active(
                        request.getActive() == null
                                || request.getActive()
                )
                .build();

        return mapToResponse(
                newsRepository.save(news)
        );
    }

    @Override
    public NewsResponseDto updateNews(
            Long id,
            NewsRequestDto request) {

        News news = findNews(id);

        boolean oldPublished =
                Boolean.TRUE.equals(news.getPublished());

        boolean newPublished =
                Boolean.TRUE.equals(request.getPublished());

        news.setTitle(request.getTitle().trim());
        news.setSummary(request.getSummary());
        news.setContent(request.getContent());
        news.setImageUrl(request.getImageUrl());
        news.setSourceUrl(request.getSourceUrl());
        news.setSourceName(request.getSourceName());

        news.setPublished(newPublished);

        if (!oldPublished && newPublished) {
            news.setPublishedAt(LocalDateTime.now());
        }

        if (!newPublished) {
            news.setPublishedAt(null);
        }

        if (request.getActive() != null) {
            news.setActive(request.getActive());
        }

        return mapToResponse(
                newsRepository.save(news)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public NewsResponseDto getNewsById(
            Long id) {

        return mapToResponse(
                findNews(id)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsResponseDto> getAllNews() {

        return newsRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsResponseDto> getPublishedNews() {

        return newsRepository
                .findByPublishedTrueOrderByPublishedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsResponseDto> getActiveNews() {

        return newsRepository
                .findByActiveTrueOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void publishNews(Long id) {

        News news = findNews(id);

        news.setPublished(true);
        news.setActive(true);

        if (news.getPublishedAt() == null) {
            news.setPublishedAt(
                    LocalDateTime.now()
            );
        }

        newsRepository.save(news);
    }

    @Override
    public void unpublishNews(Long id) {

        News news = findNews(id);

        news.setPublished(false);
        news.setPublishedAt(null);

        newsRepository.save(news);
    }

    @Override
    public void deactivateNews(Long id) {

        News news = findNews(id);

        news.setActive(false);

        newsRepository.save(news);
    }

    @Override
    public void deleteNews(Long id) {

        News news = findNews(id);

        newsRepository.delete(news);
    }

    private News findNews(Long id) {

        return newsRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "News not found with id: "
                                        + id
                        )
                );
    }

    private NewsResponseDto mapToResponse(
            News news) {

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