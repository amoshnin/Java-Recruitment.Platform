package com.example.demo.configuration.responses;

import org.springframework.data.domain.Page;

import java.util.List;

public class PaginatedResponse<T> {
    public int pageSize;
    public int totalNumberOfPages;
    public List<T> response;

    public PaginatedResponse(Page<T> page) {
        this.totalNumberOfPages = page.getTotalPages() - 1;
        this.pageSize = page.getNumberOfElements();
        this.response = page.getContent();
    }
}