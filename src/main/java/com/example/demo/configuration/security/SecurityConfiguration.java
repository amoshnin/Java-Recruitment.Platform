package com.example.demo.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;

    @Bean
    PasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(this.bcryptPasswordEncoder());
        provider.setUserDetailsService(this.userService);
        return provider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(this.daoAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                // (future) CANDIDATE can create an account for himself (in other words, anyone can create a CANDIDATE account)
                .antMatchers(HttpMethod.POST,"/api/candidates/item").anonymous()
                // ADMIN can view detail of any candidate
                .antMatchers(HttpMethod.GET,"/api/candidates/item/{candidateId}").hasRole("ADMIN")
                // CANDIDATE can view details of himself
                .antMatchers(HttpMethod.GET,"/api/candidates/item/{candidateId}").hasRole("CANDIDATE")

                // ADMIN can create accounts for recruiters
                .antMatchers(HttpMethod.POST,"/api/recruiters/item").hasRole("ADMIN")
                // RECRUITER can view details of himself
                .antMatchers(HttpMethod.GET,"/api/recruiters/item/{recruiterId}").hasRole("RECRUITER")
                // ADMIN can view details of any recruiter
                .antMatchers(HttpMethod.GET,"/api/recruiters/item/{recruiterId}").hasRole("ADMIN")

                // RECRUITER can create a job
                .antMatchers(HttpMethod.POST,"/api/jobs/item").hasRole("RECRUITER")
                // RECRUITER can update his existing job (importantly, job must already exist)
                .antMatchers(HttpMethod.PUT,"/api/jobs/item").hasRole("RECRUITER")
                // ADMIN can update any existing job (importantly, job must already exist)
                .antMatchers(HttpMethod.PUT,"/api/jobs/item").hasRole("ADMIN")
                // RECRUITER can view details of his job (importantly, only his job)
                .antMatchers(HttpMethod.GET,"/api/jobs/item/{jobId}").hasRole("RECRUITER")
                // ADMIN can view details of any job
                .antMatchers(HttpMethod.GET,"/api/jobs/item/{jobId}").hasRole("ADMIN")
                // RECRUITER can list all his jobs (importantly, only his jobs)
                .antMatchers(HttpMethod.GET,"/api/jobs/list").hasRole("RECRUITER")
                // ADMIN can list all jobs (importantly, all jobs)
                .antMatchers(HttpMethod.GET,"/api/jobs/listAll").hasRole("ADMIN")
                .anyRequest().authenticated().and().httpBasic();
    }
}