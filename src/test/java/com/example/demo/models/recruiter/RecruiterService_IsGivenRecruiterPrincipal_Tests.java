package com.example.demo.models.recruiter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class RecruiterService_IsGivenRecruiterPrincipal_Tests {
    @Autowired
    private RecruiterService service;

    @MockBean
    private RecruiterRepository recruiterRepository;

    @Test
    public void isGivenRecruiterPrincipal() {
    }
}