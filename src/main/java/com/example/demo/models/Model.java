package com.example.demo.models;

import java.util.List;

public interface Model {
    String getEmail();
    String getPassword();
    String getRole();
    List<String> getPermissions();
    Long getId();
}
