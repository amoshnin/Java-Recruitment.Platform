package com.example.demo.models.candidate;

import com.example.demo.models.admin.Admin;
import com.example.demo.models.admin.AdminRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CandidateServiceTest {
    @Autowired
    private CandidateService service;

    @MockBean
    private CandidateRepository candidateRepository;

    @MockBean
    private AdminRepository adminRepository;

    @MockBean
    private Principal principal;

    @Test
    public void isGivenCandidatePrincipal() {
    }

    @Test
    public void add() {
    }

    @Test
    public void getItem() {
        Long candidateId = 1L;
        Long adminId = 2L;
        String adminEmail = "admin@gmail.com";
        String adminPassword = "123456";
        Candidate candidate = new Candidate(
                candidateId,
                "candidate@gmail.com",
                "",
                "",
                "",
                "",
                "");

        Mockito.when(this.principal.getName()).thenReturn(adminEmail);
        Mockito.when(this.adminRepository.findAdminByEmail(adminEmail)).thenReturn(Optional.of(
                new Admin(adminId, adminEmail, adminPassword)
        ));
        Mockito.when(this.candidateRepository.findById(candidateId))
                .thenReturn(Optional.of(candidate));
        assertEquals(candidate, service.getItem(candidateId, this.principal));
    }
}