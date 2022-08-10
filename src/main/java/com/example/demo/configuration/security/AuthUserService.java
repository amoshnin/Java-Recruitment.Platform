package com.example.demo.configuration.security;

import com.example.demo.models.Model;
import com.example.demo.models.admin.Admin;
import com.example.demo.models.admin.AdminRepository;
import com.example.demo.models.candidate.Candidate;
import com.example.demo.models.candidate.CandidateRepository;
import com.example.demo.models.recruiter.Recruiter;
import com.example.demo.models.recruiter.RecruiterRepository;
import com.example.demo.models.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthUserService implements UserDetailsService {
    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Model user = this.userService.findUserByEmail(email);
        AuthUserPrincipal userPrincipal = new AuthUserPrincipal(user);
        return userPrincipal;
    }
}

