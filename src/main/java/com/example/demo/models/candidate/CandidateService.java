package com.example.demo.models.candidate;

import com.example.demo.configuration.exceptions.FoundException;
import com.example.demo.configuration.exceptions.NotFoundException;
import com.example.demo.models.admin.AdminService;
import com.example.demo.models.recruiter.Recruiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class CandidateService {
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private AdminService adminService;

    public boolean isGivenCandidatePrincipal(Principal principal, Long candidateId) {
        Optional<Candidate> row = this.candidateRepository.findCandidateByEmail(principal.getName());
        Candidate candidate = row.get();
        return candidate.getId() == candidateId;
    }

    public Candidate add(Candidate candidate) {
        if (this.candidateRepository.findCandidateByEmail(candidate.getEmail()).isPresent()) {
            throw new FoundException(String.format("Candidate with email: '%s' already exists. Emails must be unique.", candidate.getEmail()));
        }
        candidate.setPassword(new BCryptPasswordEncoder().encode(candidate.getPassword()));
        return this.candidateRepository.save(candidate);
    }

    public Candidate getItem(Long candidateId, Principal principal) {
        Optional<Candidate> row = this.candidateRepository.findById(candidateId);
        if (row.isPresent()) {
            boolean usedIsAdmin = this.adminService.isGivenUserAdmin(principal);
            boolean userIsRecruiter = this.isGivenCandidatePrincipal(principal, candidateId);
            return row.get();
        } else {
            throw new NotFoundException(String.format("Candidate with ID: %s doesn't exist", candidateId));
        }
    }
}
