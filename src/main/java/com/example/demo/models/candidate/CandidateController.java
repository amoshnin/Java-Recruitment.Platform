package com.example.demo.models.candidate;

import com.example.demo.models.recruiter.Recruiter;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;

@RestController
@RequestMapping(path = "api/candidates")
public class CandidateController {
    @Autowired
    private CandidateService candidateService;

    @Operation(summary = "Endpoint (for ADMIN to view details of any candidate) OR (for CANDIDATE to view details of himself)", description = "", tags = {"candidates"})
    @GetMapping(path = "item/{candidateId}")
    public Candidate getItem(@PathVariable Long candidateId, Principal principal) {
        return this.candidateService.getItem(candidateId, principal);
    }

    @Operation(summary = "Endpoint (for anyone unauthorised to create a CANDIDATE)", description = "", tags = {"candidates"})
    @PostMapping(path = "item")
    public ResponseEntity<Object> add(@Valid @RequestBody Candidate candidate) {
        Candidate newCandidate = this.candidateService.add(candidate);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{candidateId}")
                .buildAndExpand(newCandidate.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
