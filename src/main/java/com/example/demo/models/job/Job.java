package com.example.demo.models.job;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "jobs")
public class Job {
    // General
    @Id
    @Column(name = "job_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String shortcode;
    private String code;
    private String state;
    private String department;

    public String getFullTitle() {
        return String.format("%s - %s", this.title, this.code);
    }

    // Location
    private String country;
    private String countryCode;
    private String region;
    private String regionCode;
    private String city;
    private String zipCode;

    public String getLocationString() {
        return String.format("%s, %s, %s", this.city, this.region, this.country);
    }

    // Date
    @CreatedDate
    private LocalDate createdAt;
    @LastModifiedDate
    private LocalDate modifiedAt;
}
