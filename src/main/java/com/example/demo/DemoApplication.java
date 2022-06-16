package com.example.demo;

import com.example.demo.models.admin.Admin;
import com.example.demo.models.admin.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.transaction.Transactional;

@SpringBootApplication
@Transactional
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initialData(AdminRepository adminRepository) {
		return args -> {
			try { adminRepository.save(new Admin("test@gmail.com", new BCryptPasswordEncoder().encode("123456"))); } catch(Exception e) {}
		};
	}
}
