package com.andabazaar.mapper;

import org.mapstruct.Mapper;

import com.andabazaar.dto.news.NewsResponseDto;
import com.andabazaar.entity.News;

@Mapper(componentModel = "spring")
public interface NewsMapper {

    NewsResponseDto toDto(News news);
}
