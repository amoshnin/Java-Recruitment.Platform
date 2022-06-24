package com.example.demo.integration.job;

import com.example.demo.models.admin.Admin;
import com.example.demo.models.admin.AdminRepository;
import com.example.demo.models.recruiter.Recruiter;
import com.example.demo.models.recruiter.RecruiterRepository;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.yml")
public class JobController_Update_Tests {
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
    public void shouldUpdateJob_whenUserIsAdmin() throws Exception {
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

        JSONObject newObj = new JSONObject();
        newObj.put("id", 1);
        newObj.put("title", "New Title");
        newObj.put("code", "New Code");
        MockHttpServletResponse newResponse = this.mockMvc.perform(put("/api/jobs/item")
                        .with(user("test@gmail.com").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newObj.toString()))
                .andDo(print())
                .andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), newResponse.getStatus()); // OK => 200
        assertEquals("http://localhost/api/jobs/item/1", newResponse.getHeader("Location"));

        ResultActions getResult = this.mockMvc.perform(get("/api/jobs/item/1")
                        .with(user("test@gmail.com").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newObj.toString()));
        MockHttpServletResponse getResponse = getResult.andReturn().getResponse();
        getResult
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("New Title")))
                .andExpect(jsonPath("$.code", is("New Code")));
        assertEquals(HttpStatus.OK.value(), getResponse.getStatus()); // OK => 200
    }
}
