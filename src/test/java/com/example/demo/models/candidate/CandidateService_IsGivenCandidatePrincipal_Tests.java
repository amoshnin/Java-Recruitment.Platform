package com.example.demo.models.candidate;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CandidateService_IsGivenCandidatePrincipal_Tests {
    @Autowired
    private CandidateService service;

    @MockBean
    private CandidateRepository candidateRepository;

    @Mock
    private Principal principal;

    // CONSTANTS
    String randomEmail = "random@gmail.com";

    Long candidateId = 1L;
    String candidateEmail = "firstCandidate@gmail.com";
    Candidate candidate = new Candidate(this.candidateId, this.candidateEmail, "");

    @Test
    public void shouldReturnTrue_whenUserIsThatCandidate() {
        // define principal email => candidate
        Mockito.when(this.principal.getName()).thenReturn(this.candidateEmail);
        // define CANDIDATE.findCandidateByEmail (main)
        Mockito.when(this.candidateRepository.findCandidateByEmail(this.candidateEmail)).thenReturn(Optional.of(this.candidate));
        // assert (equals)
        assertEquals(true, this.service.isGivenCandidatePrincipal(this.principal, this.candidateId));
    }

    @Test
    public void shouldReturnFalse_whenUserIsNotThatCandidate() {
        // define principal email => random
        Mockito.when(this.principal.getName()).thenReturn(this.randomEmail);
        // define CANDIDATE.findCandidateByEmail (main)
        Mockito.when(this.candidateRepository.findCandidateByEmail(this.candidateEmail)).thenReturn(Optional.of(this.candidate));
        // assert (equals)
        assertEquals(false, this.service.isGivenCandidatePrincipal(this.principal, this.candidateId));
    }

    @Test
    public void shouldReturnFalse_whenThatCandidateDoesNotExist() {
        // assert (equals)
        assertEquals(false, this.service.isGivenCandidatePrincipal(this.principal, this.candidateId));
    }
}