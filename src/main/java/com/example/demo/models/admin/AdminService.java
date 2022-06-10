package com.example.demo.models.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    public boolean isGivenUserAdmin(Principal principal) {
        return this.adminRepository.findAdminByEmail(principal.getName()).isPresent();
    }
}
