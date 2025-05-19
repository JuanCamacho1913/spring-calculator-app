package com.calculator.exception.error;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ElementNotFoundExceptionTest {

    @Test
    void noArgsConstructorTest() {
        ElementNotFoundException exception = new ElementNotFoundException();
        assertNull(exception.getMessage());
    }

    @Test
    void constructorWithCauseTest() {
        Throwable cause = new RuntimeException("Root cause");
        ElementNotFoundException exception = new ElementNotFoundException(cause);

        assertEquals("java.lang.RuntimeException: Root cause", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void constructorWithMessageAndCauseTest() {
        String message = "Element not found in DB";
        Throwable cause = new IllegalArgumentException("ID invalid");

        ElementNotFoundException exception = new ElementNotFoundException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
