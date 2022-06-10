package com.example.demo.models.recruiter;

import com.example.demo.configuration.exceptions.FoundException;
import com.example.demo.models.candidate.Candidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class RecruiterService {
    @Autowired
    private RecruiterRepository recruiterRepository;

    public boolean isGivenRecruiterPrincipal(Principal principal, Long recruiterId) {
        Optional<Recruiter> row = this.recruiterRepository.findRecruiterByEmail(principal.getName());
        Recruiter recruiter = row.get();
        return recruiter.getId() == recruiterId;
    }

    public Recruiter add(Recruiter candidate) {
        if (this.recruiterRepository.findRecruiterByEmail(candidate.getEmail()).isPresent()) {
            throw new FoundException(String.format("Recruiter with email: '%s' already exists. Emails must be unique.", candidate.getEmail()));
        }
        candidate.setPassword(new BCryptPasswordEncoder().encode(candidate.getPassword()));
        return this.recruiterRepository.save(candidate);
    }
}
