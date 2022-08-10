package com.example.demo.models.job;

import com.example.demo.configuration.exceptions.GenericException;
import com.example.demo.configuration.exceptions.NotFoundException;
import com.example.demo.configuration.pagination.PaginationObject;
import com.example.demo.configuration.pagination.SortObject;
import com.example.demo.models.recruiter.Recruiter;
import com.example.demo.models.recruiter.RecruiterService;
import com.example.demo.models.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RecruiterService recruiterService;

    public Job add(Job job, Principal principal) {
        Recruiter recruiter = this.recruiterService.getItemByEmail(principal);
        job.setRecruiter(recruiter);
        return this.jobRepository.save(job);
    }

    public Job update(Job job, Principal principal) {
        System.out.println(job.getId());
        // method below will verify whether principal is (admin / creator of job)
        Job item = this.getItem(job.getId(), principal);

        if (!job.getTitle().isEmpty()) {
            item.setTitle(job.getTitle());
        }
        if (job.getShortcode() != null && !job.getShortcode().isEmpty()) {
            item.setShortcode(job.getShortcode());
        }
        if (job.getCode() != null && !job.getCode().isEmpty()) {
            item.setCode(job.getCode());
        }
        if (job.getState() != null && !job.getState().isEmpty()) {
            item.setState(job.getState());
        }
        if (job.getDepartment() != null && !job.getDepartment().isEmpty()) {
            item.setDepartment(job.getDepartment());
        }
        if (job.getCountry() != null && !job.getCountry().isEmpty()) {
            item.setCountry(job.getCountry());
        }
        if (job.getCountryCode() != null && !job.getCountryCode().isEmpty()) {
            item.setCountryCode(job.getCountryCode());
        }
        if (job.getRegion() != null && !job.getRegion().isEmpty()) {
            item.setRegion(job.getRegion());
        }
        if (job.getRegionCode() != null && !job.getRegionCode().isEmpty()) {
            item.setRegionCode(job.getRegionCode());
        }
        if (job.getCity() != null && !job.getCity().isEmpty()) {
            item.setCity(job.getCity());
        }
        if (job.getZipCode() != null && !job.getZipCode().isEmpty()) {
            item.setZipCode(job.getZipCode());
        }

        return this.jobRepository.save(item);
    }

    public Job getItem(Long jobId, Principal principal) {
        Optional<Job> row = this.jobRepository.findById(jobId);
        System.out.println("present: " + row.isPresent());
        if (row.isPresent()) {
            Job job = row.get();
            boolean userIsAdmin = this.userService.isGivenUserAdmin(principal);
            boolean userIsCreatorOfJob = this.recruiterService.isGivenRecruiterPrincipal(principal, job.getRecruiter().getId());
            if (userIsAdmin) {
                return job;
            } else {
                throw new GenericException(String.format("You must be ADMIN or owner of the job with ID: %s to view details of it", job.getId()));
            }
        } else {
            throw new NotFoundException(String.format("Job with ID: %s doesn't exist", jobId));
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

    public Page<Job> getMyListAsRecruiter(
            Principal principal,
            PaginationObject paginationObject,
            Optional<SortObject> sortObject) {
        Pageable pager = this.createPager(paginationObject, sortObject);
        System.out.println(pager.getPageNumber());
        System.out.println( pager.getPageSize());
        System.out.println(pager.getOffset());
        Long principalId = this.userService.findUserByEmail(principal.getName()).getId();
        Page<Job> page = this.jobRepository.findAllByRecruiter_Id(principalId, pager);
        return page;
    }

    public Page<Job> getAllListAsAdmin(
            Principal principal,
            PaginationObject paginationObject,
            Optional<SortObject> sortObject) {
        boolean userIsAdmin = this.userService.isGivenUserAdmin(principal);
        if (userIsAdmin) {
            Pageable pager = this.createPager(paginationObject, sortObject);
            Page<Job> page = this.jobRepository.findAll(pager);
            return page;
        } else {
            throw new GenericException("You must be ADMIN to view all jobs");
        }
    }
}
