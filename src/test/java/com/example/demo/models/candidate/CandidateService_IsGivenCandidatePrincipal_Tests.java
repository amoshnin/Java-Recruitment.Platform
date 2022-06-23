package com.example.demo.models.candidate;

import com.example.demo.models.admin.AdminRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.Principal;

@RunWith(SpringRunner.class)
@SpringBootTest
class CandidateService_IsGivenCandidatePrincipal_Tests {
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
    public void isGivenCandidatePrincipal() {

    }
}