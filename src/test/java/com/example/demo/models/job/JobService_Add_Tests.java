package com.example.demo.models.job;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JobService_Add_Tests {
    @Autowired
    private JobService service;

    @MockBean
    private JobRepository jobRepository;

    @Test
    public void add() {
        assertEquals(true, true);
    }
}
