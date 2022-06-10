package com.example.demo.models.job;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobLocation {
    private String country;
    private String countryCode;
    private String region;
    private String regionCode;
    private String city;
    private String zipCode;

    public String getLocationString() {
        return String.format("%s, %s, %s", this.city, this.region, this.country);
    }
}
