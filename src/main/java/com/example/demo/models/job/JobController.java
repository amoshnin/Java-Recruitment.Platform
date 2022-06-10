package com.example.demo.models.job;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path = "api/jobs")
public class JobController {
    @Autowired
    private JobService jobService;

    @Operation(summary = "Endpoint (for ADMIN to view details of any job) OR (for RECRUITER to view details of one of his jobs)", description = "", tags = {"jobs"})
    @GetMapping(path = "item/{jobId}")
    public Job getItem(@PathVariable Long jobId, Principal principal) {
        return this.jobService.getItem(jobId, principal);
    }

    @Operation(summary = "Endpoint (for RECRUITER to get a list of his jobs)", description = "", tags = {"jobs"})
    @GetMapping(path = "list")
    public List<Job> getMyListAsRecruiter(Principal principal) {
        return this.jobService.getMyListAsRecruiter(principal);
    }

    @Operation(summary = "Endpoint (for ADMIN to get a list of all jobs)", description = "", tags = {"jobs"})
    @GetMapping(path = "listAll")
    public List<Job> getAllListAsAdmin(Principal principal) {
        return this.jobService.getAllListAsAdmin(principal);
    }

    @Operation(summary = "Endpoint (for RECRUITER to create a new job)", description = "", tags = {"jobs"})
    @PostMapping(path = "item")
    public ResponseEntity<Object> add(@Valid @RequestBody Job job) {
        Job newJob = this.jobService.add(job);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{jobId}")
                .buildAndExpand(newJob.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Endpoint (for RECRUITER/ADMIN to update an existing job)", description = "", tags = {"jobs"})
    @PutMapping(path = "item")
    public ResponseEntity<Object> update(@Valid @RequestBody Job job, Principal principal) {
        Job newJob = this.jobService.update(job, principal);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{jobId}")
                .buildAndExpand(newJob.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
