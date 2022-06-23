package com.example.demo.models.job;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JobService_GetMyListAsRecruiter_Tests {
    @Autowired
    private JobService service;

    @MockBean
    private JobRepository jobRepository;

    @Test
    public void getMyListAsRecruiter() {
    }
}

