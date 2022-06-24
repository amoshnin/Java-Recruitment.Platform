package com.example.demo.integration.job;

import com.example.demo.models.recruiter.Recruiter;
import com.example.demo.models.recruiter.RecruiterRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.yml")
public class JobController_Add_Tests {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    @BeforeEach
    public void setup() { this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).apply(springSecurity()).build(); }

    final private String recruiterEmail = "alex.smith@gmail.com";
    final private String recruiterPassword = "123456";
    final private String recruiterFirstName = "Alex";
    final private String recruiterLastName = "Smith";

    @Test
    public void shouldReturn201_whenUserIsRecruiter() throws Exception {
        JSONObject recruiter_obj = new JSONObject();
        recruiter_obj.put("firstName", this.recruiterFirstName);
        recruiter_obj.put("lastName", this.recruiterLastName);
        recruiter_obj.put("email", this.recruiterEmail);
        recruiter_obj.put("password", this.recruiterPassword);
        MockHttpServletResponse recruiter_response = this.mockMvc.perform(post("/api/recruiters/item")
                .with(user("test@gmail.com").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(recruiter_obj.toString())).andDo(print()).andReturn().getResponse();
        assertEquals(HttpStatus.CREATED.value(), recruiter_response.getStatus()); // CREATED => 201
        assertEquals("http://localhost/api/recruiters/item/1", recruiter_response.getHeader(HttpHeaders.LOCATION));

        JSONObject obj = new JSONObject();
        obj.put("title", "Title");
        MockHttpServletResponse response = this.mockMvc.perform(post("/api/jobs/item")
                        .with(user("alex.smith@gmail.com").roles("RECRUITER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(obj.toString()))
                        .andDo(print())
                        .andReturn().getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus()); // CREATED => 201
        assertEquals("http://localhost/api/jobs/item/1", response.getHeader("Location"));
    }

    @Test
    @WithMockUser(username="alex.smith@gmail.com", roles={"ADMIN"})
    public void shouldReturn403_whenUserIsAdmin() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("title", "Title");
        MockHttpServletResponse response = this.mockMvc.perform(post("/api/jobs/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(obj.toString()))
                .andReturn().getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus()); // FORBIDDEN => 403
    }

    @Test
    @WithMockUser(username="alex.smith@gmail.com", roles={"CANDIDATE"})
    public void shouldReturn403_whenUserIsCandidate() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("title", "Title");
        MockHttpServletResponse response = this.mockMvc.perform(post("/api/jobs/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(obj.toString()))
                .andReturn().getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus()); // FORBIDDEN => 403
    }

    @Test
    @WithMockUser(username="alex.smith@gmail.com", roles={"RECRUITER"})
    public void shouldReturn400_whenTitleFieldIsBlank() throws Exception {
        JSONObject obj = new JSONObject();
        MockHttpServletResponse response = this.mockMvc.perform(post("/api/jobs/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(obj.toString()))
                .andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()); // BAD_REQUEST => 400
    }
}
