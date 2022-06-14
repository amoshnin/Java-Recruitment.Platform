package com.example.demo.configuration.security;

import com.example.demo.configuration.exceptions.NotFoundException;
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
public class UserService implements UserDetailsService {
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
            UserPrincipal userPrincipal = new UserPrincipal(candidate.get());
            return userPrincipal;
        } else if (recruiter.isPresent()) {
            UserPrincipal userPrincipal = new UserPrincipal(recruiter.get());
            return userPrincipal;
        } else if (admin.isPresent()) {
            UserPrincipal userPrincipal = new UserPrincipal(admin.get());
            return userPrincipal;
        } else {
            throw new UsernameNotFoundException(String.format("User with given email: %s not found", email));
        }
    }
}

