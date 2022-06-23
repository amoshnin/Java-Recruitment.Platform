package com.example.demo.models.candidate;

import com.example.demo.configuration.exceptions.FoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CandidateService_Add_Tests {
    @Autowired
    private CandidateService service;

    @MockBean
    private CandidateRepository candidateRepository;

    // CONSTANTS
    Long candidateId = 1L;
    String candidateEmail = "firstCandidate@gmail.com";
    Candidate candidate = new Candidate(this.candidateId, this.candidateEmail, "");

    @Test
    public void shouldCreateAndReturnNewCandidate_whenCandidateWithGivenEmailDoesNotExist() {
        // define CANDIDATE.save (main)
        Mockito.when(this.candidateRepository.save(this.candidate)).thenReturn(this.candidate);
        // assert (equals)
        assertEquals(this.candidate, this.service.add(this.candidate));
    }

    @Test
    public void shouldThrowFoundException_whenCandidateWithGivenEmailDoesExist() {
        // define CANDIDATE.findCandidateByEmail
        Mockito.when(this.candidateRepository.findCandidateByEmail(this.candidateEmail)).thenReturn(Optional.of(this.candidate));
        // define CANDIDATE.save (main)
        Mockito.when(this.candidateRepository.save(this.candidate)).thenReturn(this.candidate);
        // assert (throws)
        assertThrows(FoundException.class, () -> { this.service.add(this.candidate); });
    }
}