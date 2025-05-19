package com.calculator.service.impl;

import com.calculator.exception.error.ArithmeticOperationException;
import com.calculator.exception.error.ElementNotFoundException;
import com.calculator.mapper.ICalculatorOperatorMapper;
import com.calculator.persistence.entity.CalculatorOperation;
import com.calculator.persistence.entity.UserEntity;
import com.calculator.persistence.repository.ICalculatorRepository;
import com.calculator.presentation.dto.CalculationOperationRequest;
import com.calculator.presentation.dto.CalculationOperationResponse;
import com.calculator.util.OperationTypeEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class CalculationOperationServiceImplTest {

    @Mock
    private ICalculatorRepository calculatorRepository;

    @Mock
    private ICalculatorOperatorMapper calculatorOperatorMapper;

    @Mock
    private UserDetailServiceImpl userDetailServiceImpl;

    @InjectMocks
    private CalculatorOperationServiceImpl calculatorOperationService;

    final CalculatorOperation calculatorOperation = new CalculatorOperation();
    final UUID registerId = UUID.randomUUID();
    final UUID userId = UUID.randomUUID();
    final Pageable pageable = PageRequest.of(0, 10);
    String operationType;
    String startDate;
    String endDate;

    final CalculationOperationResponse response = new CalculationOperationResponse(
            registerId,
            OperationTypeEnum.ADDITION,
            BigDecimal.valueOf(10),
            BigDecimal.valueOf(10),
            BigDecimal.valueOf(20),
            LocalDateTime.now(),
            userId);


    @Test
    @DisplayName("Should find all operations")
    void findAllTest() {
        when(this.calculatorRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(calculatorOperation)));
        when(this.calculatorOperatorMapper.toResponse(calculatorOperation)).thenReturn(response);

        Page<CalculationOperationResponse> result = this.calculatorOperationService.findAll(pageable, "", "", "");

        assertEquals(1, result.getTotalElements());
        assertEquals(response, result.getContent().get(0));
        verify(calculatorRepository).findAll(pageable);
        verify(calculatorOperatorMapper).toResponse(calculatorOperation);
    }

    @Test
    @DisplayName("Should find by type of operation")
    void findByOperationType() {
        operationType = "ADDITION";

        when(this.calculatorRepository.findByOperation(OperationTypeEnum.ADDITION, pageable)).thenReturn(new PageImpl<>(List.of(calculatorOperation)));
        when(this.calculatorOperatorMapper.toResponse(calculatorOperation)).thenReturn(response);

        Page<CalculationOperationResponse> result = calculatorOperationService.findAll(pageable, operationType, "", "");

        assertEquals(1, result.getTotalElements());
        assertNotNull(result);
        verify(calculatorRepository).findByOperation(OperationTypeEnum.ADDITION, pageable);
        verify(calculatorOperatorMapper).toResponse(calculatorOperation);
    }

    @Test
    @DisplayName("Should find by date")
    void findByDate() {
        operationType = "";
        startDate = "2025-05-14T00:00:00";
        endDate = "2025-05-14T23:59:59";

        when(this.calculatorRepository.findByTimestampBetween(LocalDateTime.parse(startDate), LocalDateTime.parse(endDate), pageable)).thenReturn(new PageImpl<>(List.of(calculatorOperation)));
        when(this.calculatorOperatorMapper.toResponse(calculatorOperation)).thenReturn(response);

        Page<CalculationOperationResponse> result = calculatorOperationService.findAll(pageable, operationType, startDate, endDate);

        assertEquals(1, result.getTotalElements());
        assertNotNull(result);
        verify(calculatorRepository).findByTimestampBetween(LocalDateTime.parse(startDate), LocalDateTime.parse(endDate), pageable);
        verify(calculatorOperatorMapper).toResponse(calculatorOperation);
    }

    @Test
    @DisplayName("Should throws an exception RuntimeException")
    void findAllError() {
        String badStartDate = "invalid-date";
        endDate = "2025-05-14T10:00:00";

        RuntimeException exception = assertThrows(RuntimeException.class, () -> calculatorOperationService.findAll(pageable, "", badStartDate, endDate));
        assertTrue(exception.getMessage().startsWith("Error parsing date"));
    }

    @Test
    @DisplayName("Should not enter by type of operation")
    void findErrorIf1Case1() {
        operationType = "";
        startDate = "";
        endDate = "";

        when(calculatorRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(calculatorOperation)));
        when(calculatorOperatorMapper.toResponse(calculatorOperation)).thenReturn(response);

        Page<CalculationOperationResponse> result = calculatorOperationService.findAll(pageable, operationType, startDate, endDate);

        assertEquals(1, result.getTotalElements());
        verify(calculatorRepository).findAll(pageable);
        verify(calculatorOperatorMapper).toResponse(calculatorOperation);
    }

    @Test
    @DisplayName("Should not enter by end date")
    void findErrorIf1Case2() {
        operationType = "ADDITION";
        startDate = "";
        endDate = "2025-05-14T00:00:00";

        when(calculatorRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(calculatorOperation)));
        when(calculatorOperatorMapper.toResponse(calculatorOperation)).thenReturn(response);

        Page<CalculationOperationResponse> result = calculatorOperationService.findAll(pageable, operationType, startDate, endDate);

        assertEquals(1, result.getTotalElements());
        verify(calculatorRepository).findAll(pageable);
        verify(calculatorOperatorMapper).toResponse(calculatorOperation);
    }

    @Test
    @DisplayName("should not enter by start date.")
    void findErrorIf1Case3() {
        operationType = "ADDITION";
        startDate = "2025-05-14T00:00:00";
        endDate = "";

        when(calculatorRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(calculatorOperation)));
        when(calculatorOperatorMapper.toResponse(calculatorOperation)).thenReturn(response);

        Page<CalculationOperationResponse> result = calculatorOperationService.findAll(pageable, operationType, startDate, endDate);

        assertEquals(1, result.getTotalElements());
        verify(calculatorRepository).findAll(pageable);
        verify(calculatorOperatorMapper).toResponse(calculatorOperation);
    }

    @Test
    @DisplayName("should not enter by operationType")
    void findErrorIf2Case1() {
        operationType = "ADDITION";
        startDate = "2025-05-14T00:00:00";
        endDate = "2025-05-14T23:59:59";

        when(calculatorRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(calculatorOperation)));
        when(calculatorOperatorMapper.toResponse(calculatorOperation)).thenReturn(response);

        Page<CalculationOperationResponse> result = calculatorOperationService.findAll(pageable, operationType, startDate, endDate);

        assertEquals(1, result.getTotalElements());
        verify(calculatorRepository).findAll(pageable);
        verify(calculatorOperatorMapper).toResponse(calculatorOperation);
    }

    @Test
    @DisplayName("should not enter by startDate")
    void findErrorIf2Case2() {
        operationType = "";
        startDate = "";
        endDate = "2025-05-14T23:59:59";

        when(calculatorRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(calculatorOperation)));
        when(calculatorOperatorMapper.toResponse(calculatorOperation)).thenReturn(response);

        Page<CalculationOperationResponse> result = calculatorOperationService.findAll(pageable, operationType, startDate, endDate);

        assertEquals(1, result.getTotalElements());
        verify(calculatorRepository).findAll(pageable);
        verify(calculatorOperatorMapper).toResponse(calculatorOperation);
    }

    @Test
    @DisplayName("should not enter by endDate")
    void findErrorIf2Case3() {
        operationType = "";
        startDate = "2025-05-14T00:00:00";
        endDate = "";

        when(calculatorRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(calculatorOperation)));
        when(calculatorOperatorMapper.toResponse(calculatorOperation)).thenReturn(response);

        Page<CalculationOperationResponse> result = calculatorOperationService.findAll(pageable, operationType, startDate, endDate);

        assertEquals(1, result.getTotalElements());
        verify(calculatorRepository).findAll(pageable);
        verify(calculatorOperatorMapper).toResponse(calculatorOperation);
    }

    @Test
    @DisplayName("Should find the Id")
    void findByIdTest() {
        calculatorOperation.setId(registerId);

        when(this.calculatorRepository.findById(registerId)).thenReturn(Optional.of(calculatorOperation));
        when(this.calculatorOperatorMapper.toResponse(calculatorOperation)).thenReturn(response);

        CalculationOperationResponse result = calculatorOperationService.findById(registerId);

        assertNotNull(result);
        assertEquals(response, result);
        verify(calculatorRepository).findById(registerId);
        verify(calculatorOperatorMapper).toResponse(calculatorOperation);
    }

    @Test
    @DisplayName("Should there be an exception for the Id")
    void findByIdErrorTest() {

        when(this.calculatorRepository.findById(registerId)).thenReturn(Optional.empty());

        ElementNotFoundException exception =
                assertThrows(ElementNotFoundException.class, () -> calculatorOperationService.findById(registerId));

        assertEquals("Operation whit " + registerId + " does not exist", exception.getMessage());
        verify(calculatorRepository).findById(registerId);
    }

    @ParameterizedTest
    @MethodSource("operationArgumentsProvider")
    @DisplayName("Should save the operations")
    void saveOperationTest(OperationTypeEnum operation, BigDecimal operatingA, BigDecimal operatingB, BigDecimal expectedResult) {

        UserEntity idUser = new UserEntity();
        idUser.setId(UUID.randomUUID());

        CalculationOperationRequest request = new CalculationOperationRequest(operation, operatingA, operatingB);

        CalculatorOperation saveCalculation = new CalculatorOperation(
                registerId,
                OperationTypeEnum.ADDITION,
                operatingA,
                operatingB,
                expectedResult,
                LocalDateTime.now(),
                idUser);

        CalculationOperationResponse expectedResponse = new CalculationOperationResponse(
                registerId, operation, operatingA, operatingB, expectedResult, LocalDateTime.now(), userId
        );

        when(userDetailServiceImpl.getCurrentUsername()).thenReturn(idUser);
        when(calculatorOperatorMapper.toEntity(request)).thenReturn(calculatorOperation);
        when(calculatorRepository.save(calculatorOperation)).thenReturn(saveCalculation);
        when(calculatorOperatorMapper.toResponse(saveCalculation)).thenReturn(expectedResponse);

        CalculationOperationResponse result = calculatorOperationService.saveCalculation(request);

        assertNotNull(result);
        assertEquals(expectedResult, result.result());
        assertEquals(operation, result.operation());
        verify(userDetailServiceImpl).getCurrentUsername();
        verify(calculatorOperatorMapper).toEntity(request);
        verify(calculatorRepository).save(calculatorOperation);
        verify(calculatorOperatorMapper).toResponse(saveCalculation);
    }

    static Stream<Arguments> operationArgumentsProvider(){
        return Stream.of(
                Arguments.of(OperationTypeEnum.ADDITION, new BigDecimal("10.00"), new BigDecimal("5.00"), new BigDecimal("15.00")),

                Arguments.of(OperationTypeEnum.SUBTRACTION, new BigDecimal("10.00"), new BigDecimal("3.00"), new BigDecimal("7.00")),

                Arguments.of(OperationTypeEnum.MULTIPLICATION, new BigDecimal("2.00"), new BigDecimal("4.00"), new BigDecimal("8.00")),

                Arguments.of(OperationTypeEnum.DIVISION, new BigDecimal("10.00"), new BigDecimal("4.00"), new BigDecimal("2.50")),

                Arguments.of(OperationTypeEnum.POWER, new BigDecimal("2.00"), new BigDecimal("3.00"), new BigDecimal("8.00")),

                Arguments.of(OperationTypeEnum.SQUARE_ROOT, new BigDecimal("9.00"), BigDecimal.ZERO, new BigDecimal("3.00"))
        );
    }

    @Test
    @DisplayName("Should fall into the ArithmeticException for dividing by zero")
    void divisionByZero(){
        CalculationOperationRequest request = new CalculationOperationRequest(
                OperationTypeEnum.DIVISION,
                BigDecimal.TEN,
                BigDecimal.ZERO
        );

        ArithmeticException exception = assertThrows(ArithmeticException.class, () -> calculatorOperationService.saveCalculation(request));

        assertEquals("Cannot be divided by zero.", exception.getMessage());
    }

    @Test
    @DisplayName("Should fall into the ArithmeticException for negative number and zero")
    void squareRootOfZeroOrNegativeNumber(){
        CalculationOperationRequest request = new CalculationOperationRequest(
                OperationTypeEnum.SQUARE_ROOT,
                new BigDecimal("-4"),
                BigDecimal.ZERO
        );

        ArithmeticException exception = assertThrows(ArithmeticException.class, () -> calculatorOperationService.saveCalculation(request));
        assertEquals("Cannot calculate square root of 0 or negative number.", exception.getMessage());
    }

    @Test
    @DisplayName("Should fall into the ArithmeticOperationException because operation don't exist")
    void exceptionDefault(){
        CalculationOperationRequest request = new CalculationOperationRequest(
                OperationTypeEnum.UNKNOWN,
                BigDecimal.ONE,
                BigDecimal.ONE
        );

        ArithmeticOperationException exception = assertThrows(ArithmeticOperationException.class, () -> calculatorOperationService.saveCalculation(request));
        assertEquals("Arithmetic operation don't exist", exception.getMessage());
    }

    @Test
    @DisplayName("Should delete is succesfully")
    void deleteTest() {

        calculatorOperation.setId(registerId);
        calculatorOperation.setResult(BigDecimal.valueOf(30));

        when(this.calculatorRepository.findById(registerId)).thenReturn(Optional.of(calculatorOperation));

        assertEquals("Operation delete successful", calculatorOperationService.delete(registerId));
        verify(calculatorRepository).findById(registerId);
    }

    @Test
    @DisplayName("Should throws an exception deleteById")
    void deleteErrorTest() {
        when(this.calculatorRepository.findById(registerId)).thenReturn(Optional.empty());

        ElementNotFoundException exception = assertThrows(ElementNotFoundException.class, () -> {
            calculatorOperationService.delete(registerId);
        });

        assertEquals("Operation whit " + registerId + " does not exist", exception.getMessage());
        verify(calculatorRepository).findById(registerId);
    }

}









