package com.example.demo;

import com.example.demo.models.admin.Admin;
import com.example.demo.models.admin.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initialData(AdminRepository adminRepository) {
		return args -> {
			adminRepository.save(
					new Admin("test@gmail.com", "123456")
			);
		};
	}
}
