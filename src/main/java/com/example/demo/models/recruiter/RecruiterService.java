package com.example.demo.models.recruiter;

import com.example.demo.configuration.exceptions.FoundException;
import com.example.demo.models.candidate.Candidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RecruiterService {
    @Autowired
    private RecruiterRepository recruiterRepository;

    public Recruiter add(Recruiter candidate) {
        if (this.recruiterRepository.findRecruiterByEmail(candidate.getEmail()).isPresent()) {
            throw new FoundException(String.format("Recruiter with email: '%s' already exists. Emails must be unique.", candidate.getEmail()));
        }
        candidate.setPassword(new BCryptPasswordEncoder().encode(candidate.getPassword()));
        return this.recruiterRepository.save(candidate);
    }
}
