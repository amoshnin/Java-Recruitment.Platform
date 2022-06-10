package com.example.demo.models.role;

import com.example.demo.models.recruiter.Recruiter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @Column(name="role_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy="roles")
    private Set<Recruiter> recruiters;
}
