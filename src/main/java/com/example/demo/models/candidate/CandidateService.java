package com.example.demo.models.candidate;

import com.example.demo.configuration.exceptions.FoundException;
import com.example.demo.configuration.exceptions.NotFoundException;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CandidateService {
    @Autowired
    private CandidateRepository candidateRepository;

    public Candidate add(Candidate candidate) {
        if (this.candidateRepository.findCandidateByEmail(candidate.getEmail()).isPresent()) {
            throw new FoundException(String.format("Candidate with email: '%s' already exists. Emails must be unique.", candidate.getEmail()));
        }
        candidate.setPassword(new BCryptPasswordEncoder().encode(candidate.getPassword()));
        return this.candidateRepository.save(candidate);
    }

    public Candidate getItem(Long candidateId) {
        Optional<Candidate> row = this.candidateRepository.findById(candidateId);
        if (row.isPresent()) {
            return row.get();
        } else {
            throw new NotFoundException(String.format("Candidate with ID: %s doesn't exist", candidateId));
        }
    }
}
