package com.example.demo.models.user;

import com.example.demo.models.admin.Admin;
import com.example.demo.models.admin.AdminRepository;
import com.example.demo.models.candidate.Candidate;
import com.example.demo.models.candidate.CandidateRepository;
import com.example.demo.models.recruiter.Recruiter;
import com.example.demo.models.recruiter.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    public boolean doesEmailExist(String email) {
        Optional<Candidate> candidate = this.candidateRepository.findCandidateByEmail(email);
        Optional<Recruiter> recruiter = this.recruiterRepository.findRecruiterByEmail(email);
        Optional<Admin> admin = this.adminRepository.findAdminByEmail(email);

        return candidate.isPresent() || recruiter.isPresent() || admin.isPresent();
    }

    public boolean isGivenUserAdmin(Principal principal) {
        return this.adminRepository.findAdminByEmail(principal.getName()).isPresent();
    }
}
