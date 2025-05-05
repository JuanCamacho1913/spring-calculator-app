package com.calculator;

import com.calculator.persistence.entity.UserEntity;
import com.calculator.persistence.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class SpringCalculatorAppApplication{

	public static void main(String[] args) {
		SpringApplication.run(SpringCalculatorAppApplication.class, args);
	}
}
