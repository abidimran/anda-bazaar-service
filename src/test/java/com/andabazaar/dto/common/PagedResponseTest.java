package com.andabazaar.dto.common;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("PagedResponse Tests")
class PagedResponseTest {

    @Nested
    @DisplayName("fromList")
    class FromList {

        @Test
        @DisplayName("should create paged response from first page")
        void shouldCreateFirstPage() {
            List<String> items = List.of("a", "b", "c", "d", "e");

            PagedResponse<String> result = PagedResponse.fromList(items, 0, 2);

            assertThat(result.getContent()).containsExactly("a", "b");
            assertThat(result.getPage()).isZero();
            assertThat(result.getSize()).isEqualTo(2);
            assertThat(result.getTotalElements()).isEqualTo(5);
            assertThat(result.getTotalPages()).isEqualTo(3);
            assertThat(result.isLast()).isFalse();
        }

        @Test
        @DisplayName("should create paged response from last page")
        void shouldCreateLastPage() {
            List<String> items = List.of("a", "b", "c", "d", "e");

            PagedResponse<String> result = PagedResponse.fromList(items, 2, 2);

            assertThat(result.getContent()).containsExactly("e");
            assertThat(result.isLast()).isTrue();
        }

        @Test
        @DisplayName("should return empty content when page exceeds total")
        void shouldReturnEmptyWhenPageExceedsTotal() {
            List<String> items = List.of("a", "b");

            PagedResponse<String> result = PagedResponse.fromList(items, 5, 2);

            assertThat(result.getContent()).isEmpty();
            assertThat(result.isLast()).isTrue();
        }

        @Test
        @DisplayName("should handle empty list")
        void shouldHandleEmptyList() {
            List<String> items = List.of();

            PagedResponse<String> result = PagedResponse.fromList(items, 0, 10);

            assertThat(result.getContent()).isEmpty();
            assertThat(result.getTotalElements()).isZero();
            assertThat(result.getTotalPages()).isZero();
        }

        @Test
        @DisplayName("should handle exact page size")
        void shouldHandleExactPageSize() {
            List<String> items = List.of("a", "b", "c", "d");

            PagedResponse<String> result = PagedResponse.fromList(items, 0, 4);

            assertThat(result.getContent()).hasSize(4);
            assertThat(result.getTotalPages()).isEqualTo(1);
            assertThat(result.isLast()).isTrue();
        }

        @Test
        @DisplayName("should handle middle page")
        void shouldHandleMiddlePage() {
            List<String> items = List.of("a", "b", "c", "d", "e", "f");

            PagedResponse<String> result = PagedResponse.fromList(items, 1, 2);

            assertThat(result.getContent()).containsExactly("c", "d");
            assertThat(result.isLast()).isFalse();
        }

        @Test
        @DisplayName("should handle single element list")
        void shouldHandleSingleElement() {
            List<String> items = List.of("a");

            PagedResponse<String> result = PagedResponse.fromList(items, 0, 10);

            assertThat(result.getContent()).containsExactly("a");
            assertThat(result.getTotalElements()).isEqualTo(1);
            assertThat(result.getTotalPages()).isEqualTo(1);
            assertThat(result.isLast()).isTrue();
        }
    }
}
