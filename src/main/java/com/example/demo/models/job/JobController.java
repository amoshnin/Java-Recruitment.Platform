package com.example.demo.models.job;

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

    @GetMapping(path = "item/{jobId}")
    public Job getItem(@PathVariable Long jobId, Principal principal) {
        return this.jobService.getItem(jobId, principal);
    }

    @GetMapping(path = "list")
    public List<Job> getMyListAsRecruiter(Principal principal) {
        return this.jobService.getMyListAsRecruiter(principal);
    }

    @GetMapping(path = "listAll")
    public List<Job> getAllListAsAdmin(Principal principal) {
        return this.jobService.getAllListAsAdmin(principal);
    }

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
