package com.example.demo.models.recruiter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecruiterService {
    @Autowired
    private RecruiterRepository recruiterRepository;
}
