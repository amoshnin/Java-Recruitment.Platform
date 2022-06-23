package com.example.demo.models.recruiter;

import com.example.demo.configuration.exceptions.GenericException;
import com.example.demo.configuration.pagination.PaginationObject;
import com.example.demo.models.admin.Admin;
import com.example.demo.models.admin.AdminRepository;
import com.example.demo.models.candidate.Candidate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RecruiterService_GetAllListAsAdmin_Tests {
    @Autowired
    private RecruiterService service;

    @MockBean
    private RecruiterRepository recruiterRepository;

    @MockBean
    private AdminRepository adminRepository;

    @Mock
    private Principal principal;

    // CONSTANTS
    private PaginationObject pagination = new PaginationObject(Optional.empty(), Optional.empty());
    private Pageable pager = PageRequest.of(this.pagination.getPageNumber(), this.pagination.getPageSize());

    String randomEmail = "random@gmail.com";
    Long adminId = 1L;
    String adminEmail = "admin@gmail.com";

    List<Recruiter> recruiters = Stream.of(
            new Recruiter(1L, "recruiter1@gmail.com", "123456"),
            new Recruiter(2L, "recruiter2@gmail.com", "123456"),
            new Recruiter(3L, "recruiter3@gmail.com", "123456")
    ).collect(Collectors.toList());
    Page<Recruiter> pagedRecruiters = new PageImpl(recruiters);

    @Test
    public void shouldThrowGenericException_whenUserIsNotAdmin() {
        // define principal email => random
        Mockito.when(this.principal.getName()).thenReturn(this.randomEmail);
        // assert (throws)
        assertThrows(GenericException.class, () -> { this.service.getAllListAsAdmin(this.principal, this.pagination, Optional.empty()); });
    }

    @Test
    public void shouldReturnAllList_whenUserIsAdmin() {
        // define principal email => admin
        Mockito.when(this.principal.getName()).thenReturn(this.adminEmail);
        // define ADMIN.findAdminByEmail
        Mockito.when(this.adminRepository.findAdminByEmail(this.adminEmail)).thenReturn(Optional.of(new Admin(this.adminId, this.adminEmail, "")));
        // define RECRUITER.findAll (main)
        Mockito.when(this.recruiterRepository.findAll(org.mockito.ArgumentMatchers.isA(Pageable.class))).thenReturn(this.pagedRecruiters);
        // assert (equals)
        assertEquals(this.recruiters.size(), this.service.getAllListAsAdmin(this.principal, this.pagination, Optional.empty()).size());
        for (int i = 0; i < this.recruiters.size(); i++) {
            assertEquals(this.recruiters.get(i).getId(), this.service.getAllListAsAdmin(this.principal, this.pagination, Optional.empty()).get(i).getId());
            assertEquals(this.recruiters.get(i).getEmail(), this.service.getAllListAsAdmin(this.principal, this.pagination, Optional.empty()).get(i).getEmail());
            assertEquals(this.recruiters.get(i).getPassword(), this.service.getAllListAsAdmin(this.principal, this.pagination, Optional.empty()).get(i).getPassword());
        }
    }
}
