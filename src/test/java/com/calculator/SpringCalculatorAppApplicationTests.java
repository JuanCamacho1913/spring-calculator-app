package com.calculator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class SpringCalculatorAppApplicationTests {

	@Test
	void contextLoads() {
		assertDoesNotThrow(() -> SpringCalculatorAppApplication.main(new String[]{}));
	}

}
