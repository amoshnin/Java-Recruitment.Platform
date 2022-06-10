package com.example.demo.models.job;

import com.example.demo.configuration.exceptions.GenericException;
import com.example.demo.configuration.exceptions.NotFoundException;
import com.example.demo.models.admin.AdminService;
import com.example.demo.models.recruiter.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RecruiterService recruiterService;

    public Job add(Job job) {
        return this.jobRepository.save(job);
    }

    public Job update(Job job, Principal principal) {
        // method below will verify whether principal is (admin / creator of job)
        Job item = this.getItem(job.getId(), principal);
        if (!job.getTitle().isEmpty()) {
            item.setTitle(job.getTitle());
        }
        if (!job.getShortcode().isEmpty()) {
            item.setShortcode(job.getShortcode());
        }
        if (!job.getCode().isEmpty()) {
            item.setCode(job.getCode());
        }
        if (!job.getState().isEmpty()) {
            item.setState(job.getState());
        }
        if (!job.getDepartment().isEmpty()) {
            item.setDepartment(job.getDepartment());
        }
        if (!job.getCountry().isEmpty()) {
            item.setCountry(job.getCountry());
        }
        if (!job.getCountryCode().isEmpty()) {
            item.setCountryCode(job.getCountryCode());
        }
        if (!job.getRegion().isEmpty()) {
            item.setRegion(job.getRegion());
        }
        if (!job.getRegionCode().isEmpty()) {
            item.setRegionCode(job.getRegionCode());
        }
        if (!job.getCity().isEmpty()) {
            item.setCity(job.getCity());
        }
        if (!job.getZipCode().isEmpty()) {
            item.setZipCode(job.getZipCode());
        }

        return this.jobRepository.save(item);
    }

    public Job getItem(Long jobId, Principal principal) {
        Optional<Job> row = this.jobRepository.findById(jobId);
        if (row.isPresent()) {
            Job job = row.get();
            boolean userIsAdmin = this.adminService.isGivenUserAdmin(principal);
            boolean userIsCreatorOfJob = this.recruiterService.isGivenRecruiterPrincipal(principal, job.getRecruiter().getId());
            if (userIsCreatorOfJob || userIsAdmin) {
                return job;
            } else {
                throw new GenericException(String.format("You must be ADMIN or owner of the job with ID: %s to view details of it", job.getId()));
            }
        } else {
            throw new NotFoundException(String.format("Job with ID: %s doesn't exist", jobId));
        }
    }

    public List<Job> getMyListAsRecruiter(Principal principal) {
        return this.jobRepository.findAllByRecruiter_Email(principal.getName());
    }

    public List<Job> getAllListAsAdmin(Principal principal) {
        boolean userIsAdmin = this.adminService.isGivenUserAdmin(principal);
        if (userIsAdmin) {
            return this.jobRepository.findAll();
        } else {
            throw new GenericException("You must be ADMIN to view all jobs");
        }
    }
}
