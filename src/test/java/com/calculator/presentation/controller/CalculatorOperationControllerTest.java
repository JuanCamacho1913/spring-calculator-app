package com.calculator.presentation.controller;

import com.calculator.presentation.dto.CalculationOperationRequest;
import com.calculator.presentation.dto.CalculationOperationResponse;
import com.calculator.service.impl.CalculatorOperationServiceImpl;
import com.calculator.util.OperationTypeEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class CalculatorOperationControllerTest {

    @Mock
    private CalculatorOperationServiceImpl calculatorOperationService;

    @InjectMocks
    private CalculatorOperationController calculatorOperationController;

    @Test
    @DisplayName("Should search by type of operation or date")
    void findAllTest(){
        int page = 0;
        int size = 10;
        String operationType = "ADDITION";
        String startDate = "2024-01-01";
        String endDate = "2024-12-31";

        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").ascending());

        UUID userId = UUID.randomUUID();
        UUID idOne = UUID.randomUUID();
        UUID idTwo = UUID.randomUUID();

        CalculationOperationResponse responseOne = new CalculationOperationResponse(
                idOne,
                OperationTypeEnum.ADDITION,
                new BigDecimal("10"),
                new BigDecimal("5"),
                new BigDecimal("15"),
                LocalDateTime.now(),
                userId
        );

        CalculationOperationResponse responseTwo = new CalculationOperationResponse(
                idTwo,
                OperationTypeEnum.ADDITION,
                new BigDecimal("10"),
                new BigDecimal("30"),
                new BigDecimal("40"),
                LocalDateTime.now(),
                userId
        );

        List<CalculationOperationResponse> responseList = List.of(responseOne, responseTwo);
        Page<CalculationOperationResponse> responsePage = new PageImpl<>(responseList);

        when(calculatorOperationService.findAll(pageable, operationType, startDate, endDate)).thenReturn(responsePage);

        ResponseEntity<Page<CalculationOperationResponse>> response =
                calculatorOperationController.findAll(page, size, operationType, startDate, endDate);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getContent().size());
        verify(calculatorOperationService).findAll(pageable, operationType, startDate, endDate);
    }

    @Test
    @DisplayName("Should return me the user of this id")
    void findByIdTest(){
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        CalculationOperationResponse response = new CalculationOperationResponse(
                id,
                OperationTypeEnum.ADDITION,
                new BigDecimal("10"),
                new BigDecimal("15"),
                new BigDecimal("25"),
                LocalDateTime.now(),
                userId
        );

        when(calculatorOperationService.findById(id)).thenReturn(response);

        ResponseEntity<CalculationOperationResponse> responseEntity = calculatorOperationController.findById(id);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(response, responseEntity.getBody());
        verify(calculatorOperationService).findById(id);
    }

    @Test
    @DisplayName("Should save an operation")
    void saveCalculationTest(){
        CalculationOperationRequest request = new CalculationOperationRequest(
                OperationTypeEnum.ADDITION,
                new BigDecimal("10"),
                new BigDecimal("15")
        );

        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        CalculationOperationResponse response = new CalculationOperationResponse(
                id,
                OperationTypeEnum.ADDITION,
                new BigDecimal("10"),
                new BigDecimal("15"),
                new BigDecimal("25"),
                LocalDateTime.now(),
                userId
        );

        when(calculatorOperationService.saveCalculation(request)).thenReturn(response);

        ResponseEntity<CalculationOperationResponse> responseEntity = calculatorOperationController.saveCalculation(request);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(response, responseEntity.getBody());
        verify(calculatorOperationService).saveCalculation(request);
    }

    @Test
    @DisplayName("Should delete an operation")
    void deleteTest(){
        UUID id = UUID.randomUUID();
        String expectedMessage = "Operation delete successful";

        when(calculatorOperationService.delete(id)).thenReturn(expectedMessage);

        ResponseEntity<String> responseEntity = calculatorOperationController.delete(id);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedMessage, responseEntity.getBody());
        verify(calculatorOperationService).delete(id);
    }
}
