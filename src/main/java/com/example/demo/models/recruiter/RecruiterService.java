package com.example.demo.models.recruiter;

import com.example.demo.configuration.exceptions.FoundException;
import com.example.demo.configuration.exceptions.GenericException;
import com.example.demo.configuration.exceptions.NotFoundException;
import com.example.demo.models.admin.AdminService;
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

    @Autowired
    private AdminService adminService;

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

    public Recruiter getItem(Long recruiterId, Principal principal) {
        Optional<Recruiter> row = this.recruiterRepository.findById(recruiterId);
        if (row.isPresent()) {
            boolean userIsAdmin = this.adminService.isGivenUserAdmin(principal);
            boolean userIsRecruiter = this.isGivenRecruiterPrincipal(principal, recruiterId);
            if (userIsAdmin || userIsRecruiter) {
                return row.get();
            } else {
                throw new GenericException(String.format("You must be ADMIN or recruiter itself (with ID: %s) to view his details", recruiterId));
            }
        } else {
            throw new NotFoundException(String.format("Recruiter with ID: %s doesn't exist", recruiterId));
        }
    }
}
