package com.example.demo.models.candidate;

import com.example.demo.models.recruiter.Recruiter;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(path = "api/candidates")
public class CandidateController {
    @Autowired
    private CandidateService candidateService;

    @GetMapping(path = "item/{candidateId}")
    public Candidate getItem(@PathVariable Long candidateId) {
        return this.candidateService.getItem(candidateId);
    }

    @Operation(summary = "Endpoint (for RECRUITER to view details of himself) OR (for ADMIN to view details of any recruiter)", description = "", tags = {"recruiters"})
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
