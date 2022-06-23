package com.example.demo.integration.candidate;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.TestExecutionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.yml")
public class CandidateIntegration_Tests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void createOrder() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("firstName", "Alex");
        obj.put("lastName", "Smith");
        obj.put("email", "9alex.smith@gmail.com");
        obj.put("password", "123456");

        System.out.println(obj.toString());

        this.mockMvc.perform(post("/api/candidates/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(obj.toString()))
                .andExpect(status().isCreated());

        this.mockMvc.perform(post("/api/candidates/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(obj.toString()))
                .andExpect(status().isFound());
    }
}
