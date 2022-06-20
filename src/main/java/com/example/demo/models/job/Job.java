package com.example.demo.models.job;


import com.example.demo.models.candidate.Candidate;
import com.example.demo.models.recruiter.Recruiter;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    // Recruiter
    @JsonIgnore // we will see list of posts in user. but we don't want to see a user when requesting a post.
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="fk_recruiter_id", referencedColumnName="recruiter_id")
    private Recruiter recruiter;

    public Job(String title, String shortcode, String code, String state, String department, String country, String countryCode, String region, String regionCode, String city, String zipCode) {
        this.title = title;
        this.shortcode = shortcode;
        this.code = code;
        this.state = state;
        this.department = department;
        this.country = country;
        this.countryCode = countryCode;
        this.region = region;
        this.regionCode = regionCode;
        this.city = city;
        this.zipCode = zipCode;
    }

    public static Job getTranslated(String langCode) {
        if (langCode == "es") {
            return new Job(
                 
            );
        } else {
            return new Job(

            );
        }
    }
}
