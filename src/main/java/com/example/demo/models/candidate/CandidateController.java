package com.example.demo.models.candidate;

import com.example.demo.models.recruiter.Recruiter;
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

    @PostMapping(path = "item")
    public ResponseEntity<Object> add(@Valid @RequestBody Candidate candidate) {
        System.out.println("here");
        System.out.println(candidate.getEmail() + " "+  candidate.getPassword());
        Candidate newCandidate = this.candidateService.add(candidate);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{candidateId}")
                .buildAndExpand(newCandidate.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
