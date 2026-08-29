package com.andabazaar.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.common.PagedResponse;
import com.andabazaar.dto.news.NewsRequestDto;
import com.andabazaar.dto.news.NewsResponseDto;
import com.andabazaar.service.NewsService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "News", description = "News article management")
@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @Operation(summary = "Create News")
    @PostMapping
    public ResponseEntity<NewsResponseDto> createNews(@Valid @RequestBody NewsRequestDto request) {

 return ResponseEntity.status(HttpStatus.CREATED).body(newsService.createNews(request));
    }

    @Operation(summary = "Update News")
    @PutMapping("/{id}")
    public ResponseEntity<NewsResponseDto> updateNews(@PathVariable Long id, @Valid @RequestBody NewsRequestDto request) {

 return ResponseEntity.ok(newsService.updateNews(id, request));
    }

    @Operation(summary = "Get News By Id")
    @GetMapping("/{id}")
    public ResponseEntity<NewsResponseDto> getNewsById(@PathVariable Long id) {

 return ResponseEntity.ok(newsService.getNewsById(id));
    }

    @Operation(summary = "Get All News")
    @GetMapping
    public ResponseEntity<PagedResponse<NewsResponseDto>>
            getAllNews(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(newsService.getAllNews(), page, size));
    }

    @Operation(summary = "Get Published News")
    @GetMapping("/published")
    public ResponseEntity<PagedResponse<NewsResponseDto>>
            getPublishedNews(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(newsService.getPublishedNews(), page, size));
    }

    @Operation(summary = "Get Active News")
    @GetMapping("/active")
    public ResponseEntity<PagedResponse<NewsResponseDto>>
            getActiveNews(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(newsService.getActiveNews(), page, size));
    }

    @Operation(summary = "Publish News")
    @PutMapping("/{id}/publish")
    public ResponseEntity<Void> publishNews(@PathVariable Long id) {

        newsService.publishNews(id);

 return ResponseEntity.ok().build();
    }

    @Operation(summary = "Unpublish News")
    @PutMapping("/{id}/unpublish")
    public ResponseEntity<Void> unpublishNews(@PathVariable Long id) {

        newsService.unpublishNews(id);

 return ResponseEntity.ok().build();
    }

    @Operation(summary = "Deactivate News")
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateNews(@PathVariable Long id) {

        newsService.deactivateNews(id);

 return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete News")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {

        newsService.deleteNews(id);

 return ResponseEntity.noContent().build();
    }
}
