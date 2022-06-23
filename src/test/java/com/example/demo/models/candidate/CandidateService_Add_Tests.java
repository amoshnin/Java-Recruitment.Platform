package com.example.demo.models.candidate;

import com.example.demo.configuration.exceptions.FoundException;
import com.example.demo.models.admin.AdminRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class CandidateService_Add_Tests {
    @Autowired
    private CandidateService service;

    @MockBean
    private CandidateRepository candidateRepository;

    // constants
    Long firstCandidateId = 1L;
    String firstCandidateEmail = "firstCandidate@gmail.com";
    Candidate firstCandidate = new Candidate(this.firstCandidateId, this.firstCandidateEmail, "");

    Long secondCandidateId = 2L;
    String secondCandidateEmail = "firstCandidate@gmail.com";
    Candidate secondCandidate = new Candidate(this.secondCandidateId, this.secondCandidateEmail, "");

    @Test
    public void shouldCreateAndReturnNewCandidate_whenCandidateWithGivenEmailDoesNotExist() {
        // define CANDIDATE.save (main)
        Mockito.when(this.candidateRepository.save(this.firstCandidate)).thenReturn(this.firstCandidate);
        // assert (equals)
        assertEquals(this.firstCandidate, this.service.add(this.firstCandidate));
    }

    @Test
    public void shouldThrowFoundException_whenCandidateWithGivenEmailDoesExist() {
        // define CANDIDATE.findCandidateByEmail
        Mockito.when(this.candidateRepository.findCandidateByEmail(this.firstCandidateEmail)).thenReturn(Optional.of(this.firstCandidate));
        // define CANDIDATE.save (main)
        Mockito.when(this.candidateRepository.save(this.firstCandidate)).thenReturn(this.firstCandidate);
        // assert (throws)
        assertThrows(FoundException.class, () -> { this.service.add(this.firstCandidate); });
    }
}