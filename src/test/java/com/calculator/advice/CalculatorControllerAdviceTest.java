package com.calculator.advice;

import com.calculator.exception.error.ElementNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculatorControllerAdviceTest {

    @InjectMocks
    private CalculatorControllerAdvice advice;

    @Test
    void handleFieldErrorsTest() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "field", "Invalid value");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Map<String, Object>> response = advice.handleFieldErrors(exception);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid operation parameters", response.getBody().get("message"));
        assertTrue(((List<?>) response.getBody().get("details")).contains("Invalid value"));
    }

    @Test
    void handleNullPointerExceptionTest() {
        NullPointerException exception = new NullPointerException("Something was null");

        ResponseEntity<Map<String, Object>> response = advice.handleNullPointerException(exception);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Invalid operation parameters", response.getBody().get("message"));
        assertTrue(((List<?>) response.getBody().get("details")).contains("Something was null"));
    }

    @Test
    void handleArithmeticExceptionTest() {
        ArithmeticException exception = new ArithmeticException("Division by zero");

        ResponseEntity<Map<String, Object>> response = advice.handleArithmeticException(exception);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid operation parameters", response.getBody().get("message"));
        assertTrue(((List<?>) response.getBody().get("details")).contains("Division by zero"));
    }

    @Test
    void elementNotFoundExceptionTest() {
        ElementNotFoundException exception = new ElementNotFoundException("Item not found");

        ResponseEntity<Map<String, Object>> response = advice.elementNotFoundException(exception);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Element not found", response.getBody().get("message"));
        assertTrue(((List<?>) response.getBody().get("details")).contains("Item not found"));
    }

    @Test
    void generalException_ShouldReturnInternalServerError() {
        Exception exception = new Exception("Something bad happened");

        ResponseEntity<Map<String, Object>> response = advice.generalException(exception);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Unknown error", response.getBody().get("message"));
        assertTrue(((List<?>) response.getBody().get("details")).contains("Something bad happened"));
    }

}
