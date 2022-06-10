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

public class UserService implements UserDetailsService {
    @Autowired
    private RecruiterRepository recruiterRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private CandidateRepository candidateRepository;

    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {
        int colon = input.indexOf(":");
        if (colon == -1) {
            throw new NotFoundException("Email must include a colon (:) to define user's role");
        } else {
            String segments[] = input.split(":");
            String role = segments[0];
            String email = segments[1];
            System.out.println(role);
            System.out.println(email);

            if (role == "RECRUITER") {
                Recruiter recruiter = this.recruiterRepository.findRecruiterByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format("Recruiter with this email: %s not found", email)));
                UserPrincipal userPrincipal = new UserPrincipal(recruiter);
                return userPrincipal;
            } else if (role == "ADMIN") {
                Admin admin = this.adminRepository.findAdminByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format("Admin with this email: %s not found", email)));
                UserPrincipal userPrincipal = new UserPrincipal(admin);
                return userPrincipal;
            } else if (role == "CANDIDATE") {
                Candidate admin = this.candidateRepository.findCandidateByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format("Candidate with this email: %s not found", email)));
                UserPrincipal userPrincipal = new UserPrincipal(admin);
                return userPrincipal;
            } else {
                throw new NotFoundException(String.format("The role provided (%s) doesn't exist", role));
            }
        }
    }
}

