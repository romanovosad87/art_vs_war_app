package com.example.artvswar.dto.response.page;

import lombok.Data;

@Data
public class PageResponse {
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private int pageNumber;
}
