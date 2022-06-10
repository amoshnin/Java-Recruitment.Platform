package com.example.demo.models.candidate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "candidates")
public class Candidate {
    @Id
    @Column(name = "candidate_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email(message = "Email is not correctly formatted")
    @Column(unique = true)
    private String email;
    @Size(min=6, message = "Password should have at least 6 characters")
    private String password;
    private String firstName;
    private String lastName;
    private String desired_title;
}
