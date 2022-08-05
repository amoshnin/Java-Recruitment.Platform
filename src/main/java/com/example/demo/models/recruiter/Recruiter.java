package com.example.demo.models.recruiter;

import com.example.demo.models.Model;
import com.example.demo.models.candidate.Candidate;
import com.example.demo.models.job.Job;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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

    @Column(unique = true)
    @Email(message = "Email is not correctly formatted")
    @NotBlank
    private String email;

    @Size(min=6, message = "Password should have at least 6 characters")
    @NotBlank
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String agency;

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final String role = "RECRUITER";

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final List<String> permissions = Arrays.asList(
            "permissions:menu-jobs",
            "permissions:menu-candidates",
            "permissions:create-job"
    );

    @OneToMany(mappedBy="recruiter")
    private List<Job> jobs;

    public Recruiter(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Recruiter(String email, String password, String firstName, String lastName, String agency) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.agency = agency;
    }

    public static Recruiter getTranslated(String langCode) {
        if (langCode == "es") {
            return new Recruiter(
               "Correo electrónico",
                    "Contraseña",
                    "Nombre",
                    "Apellido",
                    "Agencia"
            );
        } else {
            return new Recruiter(
                    "Email",
                    "Password",
                    "First name",
                    "Last name",
                    "Agency"
            );
        }
    }
}
