package com.example.demo.models.recruiter;

import com.example.demo.models.Model;
import com.example.demo.models.job.Job;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "recruiters")
public class Recruiter implements Model {
    @Id
    @Column(name="recruiter_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Email(message = "Email is not correctly formatted")
    @Column(unique = true)
    private String email;
    @Size(min=6, message = "Password should have at least 6 characters")
    private String password;
    private String firstName;
    private String lastName;

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final String role = "RECRUITER";

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final List<String> permissions = Arrays.asList(
            "permissions:menu-jobs",
            "permissions:menu-candidates"
    );

    @OneToMany(mappedBy="recruiter")
    private List<Job> jobs;
}
