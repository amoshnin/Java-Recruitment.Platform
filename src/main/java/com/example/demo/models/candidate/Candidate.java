package com.example.demo.models.candidate;

import com.example.demo.models.Model;
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
@Table(name = "candidates")
public class Candidate implements Model {
    @Id
    @Column(name = "candidate_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private String currentTitle;

    private String desiredTitle;

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final String role = "CANDIDATE";

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final List<String> permissions = Arrays.asList();

    public Candidate(String email, String password, String firstName, String lastName, String currentTitle, String desiredTitle) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.currentTitle = currentTitle;
        this.desiredTitle = desiredTitle;
    }

    public Candidate(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public static Candidate getTranslated(String langCode) {
        if (langCode == "es") {
            return new Candidate(
                    "Correo electrónico",
                    "Contraseña",
                    "Nombre",
                    "Apellido",
                    "Título actual",
                    "Título deseado"
            );
        } else {
            return new Candidate(
                    "Email",
                    "Password",
                    "First name",
                    "Last name",
                    "Current title",
                    "Desired title"
                    );
        }
    }
}
