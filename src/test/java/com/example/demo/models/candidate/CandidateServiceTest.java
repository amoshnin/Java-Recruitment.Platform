package com.example.demo.models.candidate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

class CandidateServiceTest {
    @Autowired
    private CandidateService service;

    @MockBean
    private CandidateRepository repository;

    @Test
    public void isGivenCandidatePrincipal() {
    }

    @Test
    public void add() {
    }

    @Test
    public void getItem() {
    }
}