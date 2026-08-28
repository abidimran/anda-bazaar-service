package com.andabazaar.dto.news;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsRequestDto {

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title cannot exceed 200 characters")
    private String title;

    @Size(max = 500, message = "Summary cannot exceed 500 characters")
    private String summary;

    private String content;

    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    private String imageUrl;

    @Size(max = 500, message = "Source URL cannot exceed 500 characters")
    private String sourceUrl;

    @Size(max = 100, message = "Source name cannot exceed 100 characters")
    private String sourceName;

    private Boolean published;

    private Boolean active;
}