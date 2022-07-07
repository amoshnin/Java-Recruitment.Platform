package com.example.demo.configuration.responses;

import org.springframework.data.domain.Page;

import java.util.List;

public class PaginatedResponse<T> {
    public int recordCount;
    public int totalNumberOfPages;
    public int pageNumber;
    public List<T> response;

    public PaginatedResponse(Page<T> page) {
        this.totalNumberOfPages = page.getTotalPages() - 1;
        this.recordCount = page.getNumberOfElements();
        this.response = page.getContent();
        this.pageNumber = page.getNumber();
    }
}