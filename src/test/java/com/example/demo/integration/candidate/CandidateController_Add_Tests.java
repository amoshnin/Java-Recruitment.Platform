package com.example.demo.integration.candidate;

import com.example.demo.models.admin.Admin;
import com.example.demo.models.admin.AdminRepository;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.yml")
public class CandidateController_Add_Tests {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    @BeforeEach
    public void setup() { this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).apply(springSecurity()).build(); }

    private String firstName = "Alex";
    private String lastName = "Smith";
    private String email = "alex.smith@gmail.com";
    private String password = "123456";

    @Test // SUCCESS
    void shouldReturn201_whenGivenCandidateDoesNotExistYet() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("firstName", this.firstName);
        obj.put("lastName", this.lastName);
        obj.put("email", this.email);
        obj.put("password", this.password);
        MockHttpServletResponse response = this.mockMvc.perform(post("/api/candidates/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(obj.toString()))
                .andReturn().getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus()); // CREATED => 201
        assertEquals("http://localhost/api/candidates/item/1", response.getHeader("Location"));
    }

    @Test // FAILURE
    void shouldReturn302_whenGivenCandidateDoesExistAlready() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("firstName", this.firstName);
        obj.put("lastName", this.lastName);
        obj.put("email", this.email);
        obj.put("password", this.password);
        MockHttpServletResponse response = this.mockMvc.perform(post("/api/candidates/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(obj.toString())).andReturn().getResponse();
        assertEquals(HttpStatus.FOUND.value(), response.getStatus()); // FOUND => 302
    }

    @Test // FAILURE
    void shouldReturn400_whenEmailIsBadlyFormatted() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("firstName", this.firstName);
        obj.put("lastName", this.lastName);
        obj.put("email", "lula");
        obj.put("password", this.password);
        MockHttpServletResponse response = this.mockMvc.perform(post("/api/candidates/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(obj.toString())).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()); // BAD_REQUEST => 400
    }

    @Test // FAILURE
    void shouldReturn400_whenPasswordIsTooShort() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("firstName", this.firstName);
        obj.put("lastName", this.lastName);
        obj.put("email", this.email);
        obj.put("password", "12");
        MockHttpServletResponse response = this.mockMvc.perform(post("/api/candidates/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(obj.toString())).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()); // BAD_REQUEST => 400
    }

    @Test // FAILURE
    void shouldReturn400_whenFirstNameIsBlank() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("lastName", this.lastName);
        obj.put("email", this.email);
        obj.put("password", this.password);
        MockHttpServletResponse response = this.mockMvc.perform(post("/api/candidates/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(obj.toString())).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()); // BAD_REQUEST => 400
    }

    @Test // FAILURE
    void shouldReturn400_whenLastNameIsBlank() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("firstName", this.firstName);
        obj.put("email", this.email);
        obj.put("password", this.password);
        MockHttpServletResponse response = this.mockMvc.perform(post("/api/candidates/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(obj.toString())).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()); // BAD_REQUEST => 400
    }

    @Test // FAILURE
    void shouldReturn400_whenEmailIsBlank() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("firstName", this.firstName);
        obj.put("lastName", this.lastName);
        obj.put("password", this.password);
        MockHttpServletResponse response = this.mockMvc.perform(post("/api/candidates/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(obj.toString())).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()); // BAD_REQUEST => 400
    }

    @Test // FAILURE
    void shouldReturn400_whenPasswordIsBlank() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("firstName", this.firstName);
        obj.put("lastName", this.lastName);
        obj.put("email", this.email);
        MockHttpServletResponse response = this.mockMvc.perform(post("/api/candidates/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(obj.toString())).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()); // BAD_REQUEST => 400
    }
}
