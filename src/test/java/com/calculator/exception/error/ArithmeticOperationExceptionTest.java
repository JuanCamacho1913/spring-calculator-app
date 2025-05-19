package com.calculator.exception.error;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ArithmeticOperationExceptionTest {

    @Test
    void noArgsConstructorTest() {
        ArithmeticOperationException exception = new ArithmeticOperationException();
        assertNull(exception.getMessage());
    }

    @Test
    void constructorWithCauseTest() {
        Throwable cause = new RuntimeException("Root cause");
        ArithmeticOperationException exception = new ArithmeticOperationException(cause);

        assertEquals("java.lang.RuntimeException: Root cause", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void constructorWithMessageAndCauseTest() {
        String message = "Custom arithmetic error";
        Throwable cause = new IllegalArgumentException("Invalid input");

        ArithmeticOperationException exception = new ArithmeticOperationException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
