package com.andabazaar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andabazaar.entity.News;

public interface NewsRepository
        extends JpaRepository<News, Long> {

    List<News> findByActiveTrueOrderByCreatedAtDesc();

    List<News> findByPublishedTrueAndActiveTrueOrderByPublishedAtDesc();

    List<News> findByPublishedTrueOrderByPublishedAtDesc();

    List<News> findByActiveTrueOrderByPublishedAtDesc();
}