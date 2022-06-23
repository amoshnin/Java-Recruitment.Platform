package com.example.demo.models.recruiter;

import com.example.demo.configuration.exceptions.GenericException;
import com.example.demo.configuration.exceptions.NotFoundException;
import com.example.demo.models.admin.Admin;
import com.example.demo.models.admin.AdminRepository;
import com.example.demo.models.candidate.Candidate;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class RecruiterService_GetItem_Tests {
    @Autowired
    private RecruiterService service;

    @MockBean
    private RecruiterRepository recruiterRepository;

    @MockBean
    private AdminRepository adminRepository;

    @Mock
    private Principal principal;

    // CONSTANTS
    String randomEmail = "random@gmail.com";

    Long firstRecruiterId = 1L;
    String firstRecruiterEmail = "firstRecruiter@gmail.com";
    Recruiter firstRecruiter = new Recruiter(this.firstRecruiterId, this.firstRecruiterEmail, "");

    Long secondRecruiterId = 2L;
    String secondRecruiterEmail = "firstRecruiter@gmail.com";
    Recruiter secondRecruiter = new Recruiter(this.secondRecruiterId, this.secondRecruiterEmail, "");

    Long adminId = 3L;
    String adminEmail = "admin@gmail.com";

    @Test
    public void shouldReturnRecruiter_whenUserIsAdmin() {
        // define principal email => admin
        Mockito.when(this.principal.getName()).thenReturn(this.adminEmail);
        // define ADMIN.findAdminByEmail
        Mockito.when(this.adminRepository.findAdminByEmail(this.adminEmail)).thenReturn(Optional.of(
                new Admin(this.adminId, this.adminEmail, "")
        ));
        // define RECRUITER.findById (main)
        Mockito.when(this.recruiterRepository.findById(this.firstRecruiterId))
                .thenReturn(Optional.of(this.firstRecruiter));
        // assert (equals)
        assertEquals(this.firstRecruiter, this.service.getItem(this.firstRecruiterId, this.principal));
    }

    @Test
    public void shouldReturnRecruiter_whenUserIsThatRecruiter() {
        // define principal email => firstCandidate
        Mockito.when(this.principal.getName()).thenReturn(this.firstRecruiterEmail);
        // define RECRUITER.findCandidateByEmail
        Mockito.when(this.recruiterRepository.findRecruiterByEmail(this.firstRecruiterEmail)).thenReturn(Optional.of(this.firstRecruiter));
        // define RECRUITER.findById (main)
        Mockito.when(this.recruiterRepository.findById(this.firstRecruiterId))
                .thenReturn(Optional.of(this.firstRecruiter));
        // assert (equals)
        assertEquals(this.firstRecruiter, this.service.getItem(this.firstRecruiterId, this.principal));
    }

    @Test
    public void shouldThrowGenericException_whenUserIsNeitherAdminNorThatRecruiter() {
        // define principal email => random
        Mockito.when(this.principal.getName()).thenReturn(this.randomEmail);
        // define RECRUITER.findById (main)
        Mockito.when(this.recruiterRepository.findById(this.firstRecruiterId)).thenReturn(Optional.of(this.firstRecruiter));
        // assert (throws)
        assertThrows(GenericException.class, () -> { this.service.getItem(this.firstRecruiterId, this.principal); });
    }

    @Test
    public void shouldThrowNotFoundException_whenRecruiterWithGivenIdDoesNotExist() {
        // define RECRUITER.findById (main)
        Mockito.when(this.recruiterRepository.findById(this.firstRecruiterId))
                .thenReturn(Optional.of(this.firstRecruiter));
        // assert (throws)
        assertThrows(NotFoundException.class, () -> { this.service.getItem(this.secondRecruiterId, this.principal); });
    }
}