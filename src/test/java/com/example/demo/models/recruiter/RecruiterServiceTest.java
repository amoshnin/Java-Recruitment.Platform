package com.example.demo.models.recruiter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

class RecruiterServiceTest {
    @Autowired
    private RecruiterService service;

    @MockBean
    private RecruiterRepository repository;

    @Test
    void isGivenRecruiterPrincipal() {
    }

    @Test
    void add() {
    }

    @Test
    void getItem() {
    }

    @Test
    void getAllListAsAdmin() {
    }
}