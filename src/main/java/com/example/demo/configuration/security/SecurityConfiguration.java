package com.example.demo.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthUserService userService;

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
        CustomAuthenticationFilter authFilter = new CustomAuthenticationFilter(this.authenticationManagerBean());
        authFilter.setFilterProcessesUrl("/api/auth/login");

        http
                .csrf().disable()
                .cors().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/swagger-ui/**").permitAll()
                .antMatchers(HttpMethod.GET,"/v3/api-docs/**").permitAll()
                // ADMIN/CANDIDATE/RECRUITER can login
                .antMatchers(HttpMethod.POST,"/api/auth/login").permitAll()
                // ADMIN/CANDIDATE/RECRUITER can refresh
                .antMatchers(HttpMethod.GET,"/api/auth/refresh").permitAll()
                .antMatchers(HttpMethod.GET,"/api/candidates/translation").permitAll()
                .antMatchers(HttpMethod.GET,"/api/jobs/translation").permitAll()
                .antMatchers(HttpMethod.GET,"/api/recruiters/translation").permitAll()
                // (future) CANDIDATE can create an account for himself (in other words, anyone can create a CANDIDATE account)
                .antMatchers(HttpMethod.POST,"/api/candidates/item").permitAll()
                // ADMIN can view detail of any candidate
                // CANDIDATE can view details of himself
                .antMatchers(HttpMethod.GET,"/api/candidates/item/{candidateId}").hasAnyRole("CANDIDATE", "ADMIN")
                .antMatchers(HttpMethod.GET,"/api/candidates/list").hasAnyRole("RECRUITER", "ADMIN")
                .antMatchers(HttpMethod.GET,"/api/candidates/list/{offset}/{pageSize}").hasAnyRole("RECRUITER", "ADMIN")
                .antMatchers(HttpMethod.GET,"/api/candidates/list/sorted/{sortField}/{descendingSort}").hasAnyRole("RECRUITER", "ADMIN")
                .antMatchers(HttpMethod.GET,"/api/candidates/list/sorted/{sortField}/{descendingSort}/{offset}/{pageSize}").hasAnyRole("RECRUITER", "ADMIN")
                // ADMIN can create accounts for recruiters
                .antMatchers(HttpMethod.POST,"/api/recruiters/item").hasRole("ADMIN")
                // RECRUITER can view details of himself
                // ADMIN can view details of any recruiter
                .antMatchers(HttpMethod.GET,"/api/recruiters/item/{recruiterId}").hasAnyRole("RECRUITER", "ADMIN")

                // RECRUITER can create a job
                .antMatchers(HttpMethod.POST,"/api/jobs/item").hasRole("RECRUITER")
                // ADMIN can update any existing job (importantly, job must already exist)
                // RECRUITER can update his existing job (importantly, job must already exist)
                .antMatchers(HttpMethod.PUT,"/api/jobs/item").hasAnyRole("RECRUITER", "ADMIN")
                // ADMIN can view details of any job
                // RECRUITER can view details of his job (importantly, only his job)
                .antMatchers(HttpMethod.GET,"/api/jobs/item/{jobId}").hasAnyRole("RECRUITER", "ADMIN")
                // RECRUITER can list all his jobs (importantly, only his jobs)
                .antMatchers(HttpMethod.GET,"/api/jobs/list/{recruiterId}").hasAnyRole("RECRUITER", "ADMIN")
                // ... pagination
                .antMatchers(HttpMethod.GET,"/api/jobs/list/{recruiterId}/{offset}/{pageSize}").hasAnyRole("RECRUITER", "ADMIN")
                // ... sorting
                .antMatchers(HttpMethod.GET,"/api/jobs/list/sorted/{recruiterId}/{sortField}/{descendingSort}").hasAnyRole("RECRUITER", "ADMIN")
                // ... sorting ... pagination
                .antMatchers(HttpMethod.GET,"/api/jobs/list/sorted/{recruiterId}/{sortField}/{descendingSort}/{offset}/{pageSize}").hasAnyRole("RECRUITER", "ADMIN")
                // ADMIN can list all jobs (importantly, all jobs)
                .antMatchers(HttpMethod.GET,"/api/jobs/listAll").hasRole("ADMIN")
                // ... pagination
                .antMatchers(HttpMethod.GET,"/api/jobs/listAll/{offset}/{pageSize}").hasRole("ADMIN")
                // ... sorting
                .antMatchers(HttpMethod.GET,"/api/jobs/listAll/sorted/{sortField}/{descendingSort}").hasRole("ADMIN")
                // ... sorting ... pagination
                .antMatchers(HttpMethod.GET,"/api/jobs/listAll/sorted/{sortField}/{descendingSort}/{offset}/{pageSize}").hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .addFilter(authFilter)
                .addFilterBefore(new CustomAuthorisationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}