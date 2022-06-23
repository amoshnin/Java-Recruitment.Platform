package com.example.demo.models.user;

import com.example.demo.models.admin.AdminRepository;
import com.example.demo.models.candidate.CandidateRepository;
import com.example.demo.models.candidate.CandidateService;
import com.example.demo.models.recruiter.RecruiterRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    @Autowired
    private UserService service;

    @MockBean
    private RecruiterRepository recruiterRepository;

    @MockBean
    private AdminRepository adminRepository;

    @MockBean
    private CandidateRepository candidateRepository;

    @Test
    void doesEmailExist() {
    }

    @Test
    void findUserByEmail() {
    }

    @Test
    void getPrincipalData() {
    }

    @Test
    void isGivenUserAdmin() {
    }
}