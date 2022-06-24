package com.example.demo.integration.recruiter;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.yml")
public class RecruiterController_Add_Tests {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    @BeforeEach
    public void setup() { this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).apply(springSecurity()).build(); }

    final private String recruiterEmail = "alex.smith@gmail.com";
    final private String recruiterPassword = "123456";
    final private String recruiterFirstName = "Alex";
    final private String recruiterLastName = "Smith";

    @Test // SUCCESS
    @WithMockUser(username="test@gmail.com", roles={"ADMIN"})
    public void shouldReturn201_whenRecruiterDoesNotExistYet() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("firstName", this.recruiterFirstName);
        obj.put("lastName", this.recruiterLastName);
        obj.put("email", "extra." + this.recruiterEmail);
        obj.put("password", this.recruiterPassword);
        MockHttpServletResponse firstResponse = this.mockMvc.perform(post("/api/recruiters/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(obj.toString())).andReturn().getResponse();
        assertEquals(HttpStatus.CREATED.value(), firstResponse.getStatus()); // CREATED => 201
    }

    @Test // FAILURE
    @WithMockUser(username="test@gmail.com", roles={"ADMIN"})
    public void shouldReturn302_whenRecruiterDoesExistAlready() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("firstName", this.recruiterFirstName);
        obj.put("lastName", this.recruiterLastName);
        obj.put("email", this.recruiterEmail);
        obj.put("password", this.recruiterPassword);
        MockHttpServletResponse firstResponse = this.mockMvc.perform(post("/api/recruiters/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(obj.toString())).andReturn().getResponse();
        assertEquals(HttpStatus.CREATED.value(), firstResponse.getStatus()); // CREATED => 201

        MockHttpServletResponse secondResponse = this.mockMvc.perform(post("/api/recruiters/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(obj.toString())).andReturn().getResponse();
        assertEquals(HttpStatus.FOUND.value(), secondResponse.getStatus()); // FOUND => 302
    }

    @Test // FAILURE
    @WithMockUser(username="masha.smith@gmail.com", roles={"RECRUITER"})
    public void shouldReturn403_whenUserIsRecruiter() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("firstName", this.recruiterFirstName);
        obj.put("lastName", this.recruiterLastName);
        obj.put("email", this.recruiterEmail);
        obj.put("password", this.recruiterPassword);
        MockHttpServletResponse firstResponse = this.mockMvc.perform(post("/api/recruiters/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(obj.toString())).andDo(print()).andReturn().getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), firstResponse.getStatus()); // FORBIDDEN => 403
    }

    @Test // FAILURE
    @WithMockUser(username="masha.smith@gmail.com", roles={"CANDIDATE"})
    public void shouldReturn403_whenUserIsCandidate() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("firstName", this.recruiterFirstName);
        obj.put("lastName", this.recruiterLastName);
        obj.put("email", this.recruiterEmail);
        obj.put("password", this.recruiterPassword);
        MockHttpServletResponse firstResponse = this.mockMvc.perform(post("/api/recruiters/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(obj.toString())).andDo(print()).andReturn().getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), firstResponse.getStatus()); // FORBIDDEN => 403
    }
}
