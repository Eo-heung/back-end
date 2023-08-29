package com.example.backend.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class ResponseDTO<T> {
    private List<T> items;
    private Page<T> pageItems;

    private T item;
    private String errorMessage;
    private int statusCode;

    private boolean lastPage;
    private PaginationInfo paginationInfo; // 페이지네이션 정보

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    private String token;

    @Data
    public static class PaginationInfo {

        private int totalPages;     // 전체 페이지 수
        private int currentPage;    // 현재 페이지 번호
        private long totalElements; // 전체 게시글 수
    }

}
