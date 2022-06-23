package com.example.demo.models.job;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

class JobServiceTest {
    @Autowired
    private JobService service;

    @MockBean
    private JobRepository repository;

    @Test
    void add() {
    }

    @Test
    void update() {
    }

    @Test
    void getItem() {
    }

    @Test
    void getMyListAsRecruiter() {
    }

    @Test
    void getAllListAsAdmin() {
    }
}