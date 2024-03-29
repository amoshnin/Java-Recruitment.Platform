package com.example.demo.models.recruiter;

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
public class RecruiterService {
    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private UserService userService;

    public boolean isGivenRecruiterPrincipal(Principal principal, Long recruiterId) {
        System.out.println();
        Optional<Recruiter> row = this.recruiterRepository.findRecruiterByEmail(principal.getName());
        if (row.isPresent()) {
            Recruiter recruiter = row.get();
            return recruiter.getId() == recruiterId;
        } else {
            return false;
        }
    }

    public Recruiter getItemByEmail(Principal principal) {
        Optional<Recruiter> row = this.recruiterRepository.findRecruiterByEmail(principal.getName());
        if (row.isPresent()) {
            return row.get();
        } else {
            throw new NotFoundException(String.format("Recruiter with email: %s doesn't exist", principal.getName()));
        }
    }

    public Recruiter add(Recruiter candidate) {
        candidate.setPassword(new BCryptPasswordEncoder().encode(candidate.getPassword()));
        return this.recruiterRepository.save(candidate);
    }

    public Recruiter getItemById(Long recruiterId, Principal principal) {
        Optional<Recruiter> row = this.recruiterRepository.findById(recruiterId);
        if (row.isPresent()) {
            boolean userIsAdmin = this.userService.isGivenUserAdmin(principal);
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


    public Page<Recruiter> getAllListAsAdmin(Principal principal,
                                             PaginationObject paginationObject,
                                             Optional<SortObject> sortObject) {
        boolean userIsAdmin = this.userService.isGivenUserAdmin(principal);
        if (userIsAdmin) {
            Pageable pager = this.createPager(paginationObject, sortObject);
            return this.recruiterRepository.findAll(pager);
        } else {
            throw new GenericException("You must be ADMIN to view all jobs");
        }
    }
}
