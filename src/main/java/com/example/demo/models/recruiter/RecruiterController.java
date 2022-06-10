package com.example.demo.models.recruiter;

import com.example.demo.models.candidate.Candidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(path = "api/recruiters")
public class RecruiterController {
    @Autowired
    private RecruiterService recruiterService;

    @PostMapping(path = "item")
    public ResponseEntity<Object> add(@Valid @RequestBody Recruiter recruiter) {
        Recruiter newRecruiter = this.recruiterService.add(recruiter);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{recruiterId}")
                .buildAndExpand(newRecruiter.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
