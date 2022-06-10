package com.example.demo.models.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/recruiters")
public class RoleController {
    @Autowired
    private RoleService roleService;
}
