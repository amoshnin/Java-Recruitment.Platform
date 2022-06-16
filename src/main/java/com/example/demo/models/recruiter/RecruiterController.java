package com.example.demo.models.recruiter;

import com.example.demo.configuration.exceptions.FoundException;
import com.example.demo.configuration.pagination.PaginationObject;
import com.example.demo.configuration.pagination.SortObject;
import com.example.demo.configuration.responses.PaginatedResponse;
import com.example.demo.models.candidate.Candidate;
import com.example.demo.models.job.Job;
import com.example.demo.models.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/recruiters")
public class RecruiterController {
    @Autowired
    private RecruiterService recruiterService;

    @Autowired
    private UserService userService;

    @Operation(summary = "Endpoint (for ADMIN to create a new recruiter)", description = "", tags = {"recruiters"})
    @PostMapping(path = "item")
    public ResponseEntity<Object> add(@Valid @RequestBody Recruiter recruiter) {
        if (this.userService.doesEmailExist(recruiter.getEmail())) {
            throw new FoundException(String.format("User with given email: %s already exist", recruiter.getEmail()));
        } else {
            Recruiter newRecruiter = this.recruiterService.add(recruiter);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{recruiterId}")
                    .buildAndExpand(newRecruiter.getId())
                    .toUri();
            return ResponseEntity.created(location).build();
        }
    }

    @Operation(summary = "Endpoint (for ADMIN to view details of any recruiter) OR (for RECRUITER to view details of himself)", description = "", tags = {"recruiters"})
    @GetMapping(path = "item/{recruiterId}")
    public Recruiter getItem(@RequestBody Long recruiterId, Principal principal) {
        return this.recruiterService.getItem(recruiterId, principal);
    }

    @Operation(summary = "Endpoint (for ADMIN to view list of all recruiters)", description = "Optionally may specify /{offset}/{pageSize} to implement pagination of recruiters", tags = {"recruiters"})
    @GetMapping(path = "listAll")
    public PaginatedResponse<List<Recruiter>> getAllListAsAdmin(Principal principal,
                                                         @PathVariable(required = false) Optional<Integer> offset,
                                                         @PathVariable(required = false) Optional<Integer> pageSize) {
        PaginationObject pagination = new PaginationObject(offset, pageSize);
        List<Recruiter> result = this.recruiterService.getAllListAsAdmin(
                principal,
                pagination,
                Optional.empty());
        return new PaginatedResponse(result);
    }

    @Operation(summary = "Endpoint (for ADMIN to get a list of all recruiters sorted)", description = "Obligatory must specify /{sortField}/{descendingSort} to implement sorting of recruiters. Optionally may specify /{offset}/{pageSize} to implement pagination of recruiters", tags = {"recruiters"})
    @GetMapping(value = { "listAll/sorted/{sortField}/{descendingSort}", "listAll/sorted/{sortField}/{descendingSort}/{offset}/{pageSize}" })
    public PaginatedResponse<List<Recruiter>> getAllListAsAdminSorted(
            Principal principal,
            @PathVariable(required = true) String sortField,
            @PathVariable(required = true) Boolean descendingSort,
            @PathVariable(required = false) Optional<Integer> offset,
            @PathVariable(required = false) Optional<Integer> pageSize) {
        PaginationObject pagination = new PaginationObject(offset, pageSize);
        List<Recruiter> result = this.recruiterService.getAllListAsAdmin(
                principal,
                pagination,
                Optional.of(new SortObject(sortField, descendingSort)));
        return new PaginatedResponse(result);
    }
}
