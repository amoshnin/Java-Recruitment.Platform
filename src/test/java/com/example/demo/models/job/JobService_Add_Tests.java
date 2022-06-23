package com.example.demo.models.job;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JobService_Add_Tests {
    @Autowired
    private JobService service;

    @MockBean
    private JobRepository jobRepository;

    @Test
    void add() {
    }
}
