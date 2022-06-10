package com.example.demo.models.recruiter;

import com.example.demo.models.role.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "recruiters")
public class Recruiter {
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
    // @ManyToOne => many (users) to one (role) => user can only have one role
    // @ManyToMany => many (users) to many (role) => user can have multiple roles
    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name="recruiters_roles",
            joinColumns = {@JoinColumn(name="fk_recruiter_id", referencedColumnName="recruiter_id")},
            inverseJoinColumns = {@JoinColumn(name="fk_role_id", referencedColumnName="role_id")})
    private Set<Role> roles;
}
