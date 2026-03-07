package com.hoosiercoder.dispatchtool;

import com.hoosiercoder.dispatchtool.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DispatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(DispatchApplication.class, args);
	}

}
