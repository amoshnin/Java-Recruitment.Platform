package com.example.demo.configuration.responses;

import org.springframework.data.domain.Page;

import java.util.List;

public class PaginatedResponse<T> {
    public int recordCount;
    public int totalNumberOfPages;
    public List<T> response;

    public PaginatedResponse(Page<T> page) {
        this.totalNumberOfPages = page.getTotalPages();
        this.recordCount = page.getNumberOfElements();
        this.response = page.getContent();
    }
}