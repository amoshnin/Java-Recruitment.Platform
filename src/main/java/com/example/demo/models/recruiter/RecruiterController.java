package com.example.demo.models.recruiter;

import com.example.demo.configuration.exceptions.FoundException;
import com.example.demo.models.candidate.Candidate;
import com.example.demo.models.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;

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
}
