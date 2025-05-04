package com.calculator;

import com.calculator.persistence.entity.UserEntity;
import com.calculator.persistence.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class SpringCalculatorAppApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringCalculatorAppApplication.class, args);
	}

	@Autowired
	private IUserRepository userRepository;

	@Override
	public void run(String... args) throws Exception {
		/* CREATE USERS */
		UserEntity userJuan = UserEntity.builder()
				.username("juan")
				.password("$2a$10$nrPqs/LaJ9ook6zRKhJMW.V9BvRW.Vyhi8YBOUbmsFqWNYQ5b8f/q")
				.email("juan@gmail.com")
				.isEnabled(true)
				.accountNoLocked(true)
				.accountNoExpired(true)
				.credentialNoExpired(true)
				.build();

		UserEntity userSantiago = UserEntity.builder()
				.username("santiago")
				.password("$2a$10$nrPqs/LaJ9ook6zRKhJMW.V9BvRW.Vyhi8YBOUbmsFqWNYQ5b8f/q")
				.email("santiago@gmail.com")
				.isEnabled(true)
				.accountNoLocked(true)
				.accountNoExpired(true)
				.credentialNoExpired(true)
				.build();

		userRepository.saveAll(List.of(userSantiago, userJuan));
	}
}
