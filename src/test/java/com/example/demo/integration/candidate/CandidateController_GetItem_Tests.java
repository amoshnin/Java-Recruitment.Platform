package com.example.demo.integration.candidate;

import com.example.demo.models.admin.Admin;
import com.example.demo.models.admin.AdminRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.yml")
public class CandidateController_GetItem_Tests {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    @BeforeEach
    public void setup() { this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).apply(springSecurity()).build();}
    @MockBean
    private AdminRepository adminRepository;

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
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals("http://localhost/api/candidates/item/1", response.getHeader("Location")); // CREATED => 201
    }

    @Test // SUCCESS
    @WithMockUser(username="alex.smith@gmail.com", roles={"CANDIDATE"})
    void shouldGetCandidate_whenUserIsThatCandidate() throws Exception {
        ResultActions result = this.mockMvc
                .perform(get("/api/candidates/item/1").contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
        MockHttpServletResponse response = result.andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus()); // CREATED => 200
        result
                .andExpect(jsonPath("$.firstName", is(this.firstName)))
                .andExpect(jsonPath("$.lastName", is(this.lastName)))
                .andExpect(jsonPath("$.email", is(this.email)));
    }

    @Test // SUCCESS
    @WithMockUser(username="test@gmail.com", roles={"ADMIN"})
    void shouldGetCandidate_whenUserIsAdmin() throws Exception {
        Mockito.when(this.adminRepository.findAdminByEmail("test@gmail.com")).thenReturn(Optional.of(new Admin(3L, "test@gmail.com", "123456")));
        ResultActions result = this.mockMvc
                .perform(get("/api/candidates/item/1").contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
        MockHttpServletResponse response = result.andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus()); // CREATED => 200
        result
                .andExpect(jsonPath("$.firstName", is(this.firstName)))
                .andExpect(jsonPath("$.lastName", is(this.lastName)))
                .andExpect(jsonPath("$.email", is(this.email)));
    }

    @Test // FAILURE
    @WithMockUser(username="test@gmail.com", roles={"ADMIN"})
    void shouldReturn404_whenGivenCandidateDoesNotExist() throws Exception {
        Mockito.when(this.adminRepository.findAdminByEmail("test@gmail.com")).thenReturn(Optional.of(new Admin(3L, "test@gmail.com", "123456")));
        ResultActions result = this.mockMvc
                .perform(get("/api/candidates/item/9999").contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
        MockHttpServletResponse response = result.andReturn().getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()); // NOT_FOUND => 404
    }

    @Test // FAILURE
    @WithMockUser(username="random@gmail.com", roles={"CANDIDATE"})
    void shouldReturn423_whenUserIsNeitherThatCandidateNorAdmin() throws Exception {
        ResultActions result = this.mockMvc
                .perform(get("/api/candidates/item/1").contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
        MockHttpServletResponse response = result.andReturn().getResponse();
        assertEquals(HttpStatus.LOCKED.value(), response.getStatus()); // LOCKED => 423
    }
}
