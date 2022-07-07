package com.example.demo.models.job;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findAllByRecruiter_Email(String email);
    List<Job> findAllByRecruiter_Email(String email, Pageable pager);
    Page<Job> findAllByRecruiter_Id(Long recruiterId, Pageable pager);
    List<Job> findAllByRecruiter_Id(Long recruiterId);
}