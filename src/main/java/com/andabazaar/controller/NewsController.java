package com.andabazaar.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.news.NewsRequestDto;
import com.andabazaar.dto.news.NewsResponseDto;
import com.andabazaar.service.NewsService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @PostMapping
    public ResponseEntity<NewsResponseDto> createNews(
            @Valid @RequestBody NewsRequestDto request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        newsService.createNews(request)
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<NewsResponseDto> updateNews(
            @PathVariable Long id,
            @Valid @RequestBody NewsRequestDto request) {

        return ResponseEntity.ok(
                newsService.updateNews(id, request)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsResponseDto> getNewsById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                newsService.getNewsById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<NewsResponseDto>> getAllNews() {

        return ResponseEntity.ok(
                newsService.getAllNews()
        );
    }

    @GetMapping("/published")
    public ResponseEntity<List<NewsResponseDto>>
            getPublishedNews() {

        return ResponseEntity.ok(
                newsService.getPublishedNews()
        );
    }

    @GetMapping("/active")
    public ResponseEntity<List<NewsResponseDto>>
            getActiveNews() {

        return ResponseEntity.ok(
                newsService.getActiveNews()
        );
    }

    @PutMapping("/{id}/publish")
    public ResponseEntity<Void> publishNews(
            @PathVariable Long id) {

        newsService.publishNews(id);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/unpublish")
    public ResponseEntity<Void> unpublishNews(
            @PathVariable Long id) {

        newsService.unpublishNews(id);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateNews(
            @PathVariable Long id) {

        newsService.deactivateNews(id);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(
            @PathVariable Long id) {

        newsService.deleteNews(id);

        return ResponseEntity.noContent().build();
    }
}