package com.example.demo.models.candidate;

import com.example.demo.configuration.exceptions.FoundException;
import com.example.demo.configuration.pagination.PaginationObject;
import com.example.demo.configuration.pagination.SortObject;
import com.example.demo.configuration.responses.PaginatedResponse;
import com.example.demo.models.recruiter.Recruiter;
import com.example.demo.models.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/candidates")
@Slf4j
public class CandidateController {
    @Autowired
    Environment environment;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private UserService userService;

    @Operation(summary = "Endpoint (for ADMIN to view details of any candidate) OR (for CANDIDATE to view details of himself)", description = "", tags = {"candidates"})
    @GetMapping(path = "item/{candidateId}")
    public Candidate getItem(@PathVariable Long candidateId, Principal principal) {
        return this.candidateService.getItem(candidateId, principal);
    }

    @Operation(summary = "Endpoint (for ADMIN to view list of all candidates)", description = "", tags = {"candidates"})
    @GetMapping(value = {"list", "list/{offset}/{pageSize}"})
    public PaginatedResponse<Candidate> getListAsAdmin(Principal principal,
                                            @PathVariable(required = false) Optional<Integer> offset,
                                          @PathVariable(required = false) Optional<Integer> pageSize) {
        PaginationObject pagination = new PaginationObject(offset, pageSize);
        Page<Candidate> page = this.candidateService.getList(principal, pagination, Optional.empty());
        return new PaginatedResponse(page);
    }

    @Operation(summary = "Endpoint (for ADMIN to view list of all candidates)", description = "", tags = {"candidates"})
    @GetMapping(value = {"list/sorted/{sortField}/{descendingSort}", "list/sorted/{sortField}/{descendingSort}/{offset}/{pageSize}"})
    public PaginatedResponse<Candidate> getListAsAdminSorted(
            Principal principal,
            @PathVariable(required = true) String sortField,
            @PathVariable(required = true) Boolean descendingSort,
            @PathVariable(required = false) Optional<Integer> offset,
            @PathVariable(required = false) Optional<Integer> pageSize) {
        PaginationObject pagination = new PaginationObject(offset, pageSize);
        Page<Candidate> page = this.candidateService.getList(
                principal,
                pagination,
                Optional.of(new SortObject(sortField, descendingSort)));
        return new PaginatedResponse(page);
    }

    @Operation(summary = "Endpoint (for anyone unauthorised to create a CANDIDATE)", description = "", tags = {"candidates"})
    @PostMapping(path = "item")
    public ResponseEntity<Object> add(@RequestBody @Valid Candidate candidate) {
        if (this.userService.doesEmailExist(candidate.getEmail())) {
            throw new FoundException(String.format("User with given email: %s already exist", candidate.getEmail()));
        } else {
            Candidate newCandidate = this.candidateService.add(candidate);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{candidateId}")
                    .buildAndExpand(newCandidate.getId())
                    .toUri();
            return ResponseEntity.created(location).build();
        }
    }

    @Operation(summary = "Endpoint (to retrieve translated fields of candidate)", description = "", tags = {"candidates"})
    @GetMapping(path = "translation")
    public Candidate getTranslatedFields(HttpServletRequest request) {
        Locale currentLocale = request.getLocale();
        String langCode = currentLocale.getLanguage();
        return Candidate.getTranslated(langCode);
    }
}
