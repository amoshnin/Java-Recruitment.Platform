package com.example.demo.models.candidate;

import com.example.demo.configuration.exceptions.FoundException;
import com.example.demo.configuration.exceptions.GenericException;
import com.example.demo.configuration.exceptions.NotFoundException;
import com.example.demo.configuration.pagination.PaginationObject;
import com.example.demo.configuration.pagination.SortObject;
import com.example.demo.models.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class CandidateService {
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private UserService userService;

    public boolean isGivenCandidatePrincipal(Principal principal, Long candidateId) {
        Optional<Candidate> row = this.candidateRepository.findCandidateByEmail(principal.getName());
        if (row.isPresent()) {
            Candidate candidate = row.get();
            return candidate.getId() == candidateId;
        } else {
            return false;
        }
    }
    private Pageable createPager(PaginationObject paginationObject,
                                 Optional<SortObject> sortObject) {
        Optional<Sort> sort = Optional.empty();
        if (sortObject.isPresent()) {
            if (sortObject.get().getDescendingSort()) {
                sort = Optional.of(Sort.by(sortObject.get().getSortField()).descending());
            } else {
                sort = Optional.of(Sort.by(sortObject.get().getSortField()).ascending());
            }
        }
        Pageable pager = PageRequest.of(paginationObject.getPageNumber(), paginationObject.getPageSize());
        if (sort.isPresent()) {
            pager = PageRequest.of(paginationObject.getPageNumber(), paginationObject.getPageSize(), sort.get());
        }
        return pager;
    }

    public Page<Candidate> getList(Principal principal, PaginationObject paginationObject, Optional<SortObject> sortObject) {
        boolean userIsAdmin = this.userService.isGivenUserAdmin(principal);
        if (userIsAdmin) {
            Pageable pager = this.createPager(paginationObject, sortObject);
            return this.candidateRepository.findAll(pager);
        } else {
            throw new GenericException("You must be ADMIN to view all jobs");
        }
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
            boolean userIsAdmin = this.userService.isGivenUserAdmin(principal);
            boolean userIsCandidate = this.isGivenCandidatePrincipal(principal, candidateId);
            if (userIsAdmin | userIsCandidate) {
                return row.get();
            } else {
                throw new GenericException("You cannot access this resource because you are neither a candidate of whom you're trying to get details nor you are an admin");
            }
        } else {
            throw new NotFoundException(String.format("Candidate with ID: %s doesn't exist", candidateId));
        }
    }
}
