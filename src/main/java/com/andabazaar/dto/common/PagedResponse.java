package com.andabazaar.dto.common;

import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagedResponse<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public static <T> PagedResponse<T> of(org.springframework.data.domain.Page<T> page) {
        return PagedResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    /**
     * Create a paged response from a full List by slicing it in-memory.
     * Useful when the service returns a full list but the API needs pagination.
     */
    public static <T> PagedResponse<T> fromList(List<T> list, int page, int size) {
        int total = list.size();
        int start = page * size;
        int end = Math.min(start + size, total);
        List<T> content = (start >= total) ? List.of() : list.subList(start, end);
        int totalPages = (int) Math.ceil((double) total / size);
        return PagedResponse.<T>builder()
                .content(content)
                .page(page)
                .size(size)
                .totalElements(total)
                .totalPages(totalPages)
                .last(page >= totalPages - 1)
                .build();
    }
}
