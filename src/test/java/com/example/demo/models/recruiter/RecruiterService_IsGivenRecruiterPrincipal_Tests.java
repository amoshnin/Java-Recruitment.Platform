package com.example.demo.models.recruiter;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RecruiterService_IsGivenRecruiterPrincipal_Tests {
    @Autowired
    private RecruiterService service;

    @MockBean
    private RecruiterRepository recruiterRepository;

    @Mock
    private Principal principal;

    // CONSTANTS
    String randomEmail = "random@gmail.com";

    Long recruiterId = 1L;
    String recruiterEmail = "recruiter@gmail.com";
    Recruiter recruiter = new Recruiter(this.recruiterId, this.recruiterEmail, "");

    @Test
    public void shouldReturnTrue_whenUserIsThatRecruiter() {
        // define principal email => recruiter
        Mockito.when(this.principal.getName()).thenReturn(this.recruiterEmail);
        // define RECRUITER.findCandidateByEmail (main)
        Mockito.when(this.recruiterRepository.findRecruiterByEmail(this.recruiterEmail)).thenReturn(Optional.of(this.recruiter));
        // assert (equals)
        assertEquals(true, this.service.isGivenRecruiterPrincipal(this.principal, this.recruiterId));
    }

    @Test
    public void shouldReturnFalse_whenUserIsNotThatRecruiter() {
        // define principal email => random
        Mockito.when(this.principal.getName()).thenReturn(this.randomEmail);
        // define RECRUITER.findCandidateByEmail (main)
        Mockito.when(this.recruiterRepository.findRecruiterByEmail(this.recruiterEmail)).thenReturn(Optional.of(this.recruiter));
        // assert (equals)
        assertEquals(false, this.service.isGivenRecruiterPrincipal(this.principal, this.recruiterId));
    }

    @Test
    public void shouldReturnFalse_whenThatRecruiterDoesNotExist() {
        // assert (equals)
        assertEquals(false, this.service.isGivenRecruiterPrincipal(this.principal, this.recruiterId));
    }
}