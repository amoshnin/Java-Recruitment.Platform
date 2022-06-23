package com.example.demo.models.recruiter;

import com.example.demo.configuration.exceptions.FoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RecruiterService_Add_Tests {
    @Autowired
    private RecruiterService service;

    @MockBean
    private RecruiterRepository recruiterRepository;

    // CONSTANTS
    Long recruiterId = 1L;
    String recruiterEmail = "firstCandidate@gmail.com";
    Recruiter recruiter = new Recruiter(this.recruiterId, this.recruiterEmail, "");

    @Test
    public void shouldCreateAndReturnRecruiter_whenRecruiterWithGivenEmailDoesNotExist() {
        // define CANDIDATE.save (main)
        Mockito.when(this.recruiterRepository.save(this.recruiter)).thenReturn(this.recruiter);
        // assert (equals)
        assertEquals(this.recruiter, this.service.add(this.recruiter));
    }

    @Test
    public void shouldThrowFoundException_whenRecruiterWithGivenEmailDoesExist() {
        // define CANDIDATE.findCandidateByEmail
        Mockito.when(this.recruiterRepository.findRecruiterByEmail(this.recruiterEmail)).thenReturn(Optional.of(this.recruiter));
        // define CANDIDATE.save (main)
        Mockito.when(this.recruiterRepository.save(this.recruiter)).thenReturn(this.recruiter);
        // assert (throws)
        assertThrows(FoundException.class, () -> { this.service.add(this.recruiter); });
    }
}
