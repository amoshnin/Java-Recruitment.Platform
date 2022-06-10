package com.example.demo.models.role;

import com.example.demo.models.candidate.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Candidate, Long> {
}
