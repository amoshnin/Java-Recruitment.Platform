package com.example.demo.configuration.security;

import com.example.demo.models.admin.Admin;
import com.example.demo.models.admin.AdminRepository;
import com.example.demo.models.candidate.Candidate;
import com.example.demo.models.candidate.CandidateRepository;
import com.example.demo.models.recruiter.Recruiter;
import com.example.demo.models.recruiter.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthUserService implements UserDetailsService {
    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Candidate> candidate = this.candidateRepository.findCandidateByEmail(email);
        Optional<Recruiter> recruiter = this.recruiterRepository.findRecruiterByEmail(email);
        Optional<Admin> admin = this.adminRepository.findAdminByEmail(email);

        if (candidate.isPresent()) {
            AuthUserPrincipal userPrincipal = new AuthUserPrincipal(candidate.get());
            return userPrincipal;
        } else if (recruiter.isPresent()) {
            AuthUserPrincipal userPrincipal = new AuthUserPrincipal(recruiter.get());
            return userPrincipal;
        } else if (admin.isPresent()) {
            AuthUserPrincipal userPrincipal = new AuthUserPrincipal(admin.get());
            return userPrincipal;
        } else {
            throw new UsernameNotFoundException(String.format("User with given email: %s not found", email));
        }
    }
}

