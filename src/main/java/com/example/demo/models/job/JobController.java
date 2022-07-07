package com.example.demo.models.job;

import com.example.demo.configuration.pagination.PaginationObject;
import com.example.demo.configuration.pagination.SortObject;
import com.example.demo.configuration.responses.PaginatedResponse;
import com.example.demo.models.candidate.Candidate;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

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

    @Operation(summary = "Endpoint (for RECRUITER to get a list of his jobs)", description = "Optionally may specify /{offset}/{pageSize} to implement pagination of jobs", tags = {"jobs"})
    @GetMapping(value = { "list", "list/{offset}/{pageSize}" })
    public PaginatedResponse<List<Job>> getMyListAsRecruiter(
            Principal principal,
            @PathVariable(required = false) Optional<Integer> offset,
            @PathVariable(required = false) Optional<Integer> pageSize) {
        PaginationObject pagination = new PaginationObject(offset, pageSize);
        Page<Job> page = this.jobService.getMyListAsRecruiter(
                principal,
                pagination,
                Optional.empty());
        return new PaginatedResponse(page);
    }

    @Operation(summary = "Endpoint (for RECRUITER to get a list of his jobs)", description = "Obligatory must specify /{sortField}/{descendingSort} to implement sorting of jobs. Optionally may specify /{offset}/{pageSize} to implement pagination of jobs", tags = {"jobs"})
    @GetMapping(value = { "list/sorted/{sortField}/{descendingSort}", "list/sorted/{sortField}/{descendingSort}/{offset}/{pageSize}" })
    public PaginatedResponse<List<Job>> getMyListAsRecruiterSorted(
            Principal principal,
            @PathVariable(required = true) String sortField,
            @PathVariable(required = true) Boolean descendingSort,
            @PathVariable(required = false) Optional<Integer> offset,
            @PathVariable(required = false) Optional<Integer> pageSize) {
        PaginationObject pagination = new PaginationObject(offset, pageSize);
        Page<Job> page = this.jobService.getMyListAsRecruiter(
                principal,
                pagination,
                Optional.of(new SortObject(sortField, descendingSort)));
        return new PaginatedResponse(page);
    }

    @Operation(summary = "Endpoint (for ADMIN to get a list of all jobs)", description = "Optionally may specify /{offset}/{pageSize} to implement pagination of jobs", tags = {"jobs"})
    @GetMapping(value = { "listAll", "listAll/{offset}/{pageSize}" })
    public PaginatedResponse<List<Job>> getAllListAsAdmin(
            Principal principal,
            @PathVariable(required = false) Optional<Integer> offset,
            @PathVariable(required = false) Optional<Integer> pageSize) {
        PaginationObject pagination = new PaginationObject(offset, pageSize);
        Page<Job> page = this.jobService.getAllListAsAdmin(
                principal,
                pagination,
                Optional.empty());
        return new PaginatedResponse(page);
    }

    @Operation(summary = "Endpoint (for ADMIN to get a list of all jobs sorted)", description = "Obligatory must specify /{sortField}/{descendingSort} to implement sorting of jobs. Optionally may specify /{offset}/{pageSize} to implement pagination of jobs", tags = {"jobs"})
    @GetMapping(value = { "listAll/sorted/{sortField}/{descendingSort}", "listAll/sorted/{sortField}/{descendingSort}/{offset}/{pageSize}" })
    public PaginatedResponse<List<Job>> getAllListAsAdminSorted(
            Principal principal,
            @PathVariable(required = true) String sortField,
            @PathVariable(required = true) Boolean descendingSort,
            @PathVariable(required = false) Optional<Integer> offset,
            @PathVariable(required = false) Optional<Integer> pageSize) {
        PaginationObject pagination = new PaginationObject(offset, pageSize);
        Page<Job> page = this.jobService.getAllListAsAdmin(
                principal,
                pagination,
                Optional.of(new SortObject(sortField, descendingSort)));
        return new PaginatedResponse(page);
    }

    @Operation(summary = "Endpoint (for RECRUITER to create a new job)", description = "", tags = {"jobs"})
    @PostMapping(path = "item")
    public ResponseEntity<Object> add(@Valid @RequestBody Job job, Principal principal) {
        Job newJob = this.jobService.add(job, principal);
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
        String location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{jobId}")
                .buildAndExpand(newJob.getId())
                .toUriString();
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.LOCATION, location).build();
    }

    @Operation(summary = "Endpoint (to retrieve translated fields of job)", description = "", tags = {"jobs"})
    @GetMapping(path = "translation")
    public Job getTranslatedFields(HttpServletRequest request) {
        Locale currentLocale = request.getLocale();
        String langCode = currentLocale.getLanguage();
        return Job.getTranslated(langCode);
    }
}
