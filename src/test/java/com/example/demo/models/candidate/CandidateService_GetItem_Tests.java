package com.example.demo.models.candidate;

import com.example.demo.configuration.exceptions.GenericException;
import com.example.demo.configuration.exceptions.NotFoundException;
import com.example.demo.models.admin.Admin;
import com.example.demo.models.admin.AdminRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class CandidateService_GetItem_Tests {
    @Autowired
    private CandidateService service;

    @MockBean
    private CandidateRepository candidateRepository;

    @MockBean
    private AdminRepository adminRepository;

    @Mock
    private Principal principal;

    // constants
    String randomEmail = "random@gmail.com";

    Long firstCandidateId = 1L;
    String firstCandidateEmail = "firstCandidate@gmail.com";
    Candidate firstCandidate = new Candidate(this.firstCandidateId, this.firstCandidateEmail, "");

    Long secondCandidateId = 2L;
    String secondCandidateEmail = "firstCandidate@gmail.com";
    Candidate secondCandidate = new Candidate(this.secondCandidateId, this.secondCandidateEmail, "");

    Long adminId = 3L;
    String adminEmail = "admin@gmail.com";

    @Test
    public void shouldReturnCandidate_whenUserIsAdmin() {
        // define principal email => admin
        Mockito.when(this.principal.getName()).thenReturn(this.adminEmail);
        // define ADMIN.findAdminByEmail
        Mockito.when(this.adminRepository.findAdminByEmail(this.adminEmail)).thenReturn(Optional.of(
                new Admin(this.adminId, this.adminEmail, "")
        ));
        // define CANDIDATE.findById (main)
        Mockito.when(this.candidateRepository.findById(this.firstCandidateId))
                .thenReturn(Optional.of(this.firstCandidate));
        // assert (equals)
        assertEquals(this.firstCandidate, service.getItem(this.firstCandidateId, this.principal));
    }

    @Test
    public void shouldReturnCandidate_whenUserIsThatCandidate() {
        // define principal email => firstCandidate
        Mockito.when(this.principal.getName()).thenReturn(this.firstCandidateEmail);
        // define CANDIDATE.findCandidateByEmail
        Mockito.when(this.candidateRepository.findCandidateByEmail(this.firstCandidateEmail)).thenReturn(Optional.of(this.firstCandidate));
        // define CANDIDATE.findById (main)
        Mockito.when(this.candidateRepository.findById(this.firstCandidateId))
                .thenReturn(Optional.of(this.firstCandidate));
        // assert (equals)
        assertEquals(this.firstCandidate, service.getItem(this.firstCandidateId, this.principal));
    }

    @Test
    public void shouldThrowGenericException_whenUserIsNeitherAdminNorThatCandidate() {
        // define principal email => random
        Mockito.when(this.principal.getName()).thenReturn(this.randomEmail);
        // define CANDIDATE.findById (main)
        Mockito.when(this.candidateRepository.findById(this.firstCandidateId)).thenReturn(Optional.of(this.firstCandidate));
        // assert (throws)
        assertThrows(GenericException.class, () -> { this.service.getItem(this.firstCandidateId, this.principal); });
    }

    @Test
    public void shouldThrowNotFoundException_whenCandidateWithGivenIdDoesNotExist() {
        // define CANDIDATE.findById (main)
        Mockito.when(this.candidateRepository.findById(this.firstCandidateId))
                .thenReturn(Optional.of(this.firstCandidate));
        // assert (throws)
        assertThrows(NotFoundException.class, () -> { this.service.getItem(this.secondCandidateId, this.principal); });
    }
}