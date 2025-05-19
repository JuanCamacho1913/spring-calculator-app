package com.calculator.persistence.entity;

import static org.junit.jupiter.api.Assertions.*;

import com.calculator.util.OperationTypeEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class CalculatorOperationTest {

    @Test
    void prePersistIdTest(){
        UUID id = UUID.randomUUID();

        CalculatorOperation operation = new CalculatorOperation();
        operation.setId(id);
        operation.prePersist();

        assertEquals(id, operation.getId(), "ID should remain unchanged");
    }

    @Test
    void prePersistTimestampTest(){
        CalculatorOperation operation = new CalculatorOperation();
        operation.setOperation(OperationTypeEnum.ADDITION);
        operation.setOperandA(new BigDecimal("5"));
        operation.setOperandB(new BigDecimal("10"));
        operation.setResult(new BigDecimal("15"));
        operation.setUser(new UserEntity());

        operation.prePersist();

        assertNotNull(operation.getId(), "ID should be generated");
        assertNotNull(operation.getTimestamp(), "Timestamp should be set");
    }

    @Test
    void allTest(){
        UUID id = UUID.randomUUID();
        UserEntity user = new UserEntity();
        LocalDateTime now = LocalDateTime.now();

        CalculatorOperation operation = new CalculatorOperation(
                id,
                OperationTypeEnum.ADDITION,
                new BigDecimal("3"),
                new BigDecimal("4"),
                new BigDecimal("12"),
                now,
                user
        );

        assertEquals(id, operation.getId());
        assertEquals(OperationTypeEnum.ADDITION, operation.getOperation());
        assertEquals(new BigDecimal("3"), operation.getOperandA());
        assertEquals(new BigDecimal("4"), operation.getOperandB());
        assertEquals(new BigDecimal("12"), operation.getResult());
        assertEquals(now, operation.getTimestamp());
        assertEquals(user, operation.getUser());
    }

    @Test
    @DisplayName("Should set the attributes")
    void allSettersTest(){
        UUID id = UUID.randomUUID();
        OperationTypeEnum operationType = OperationTypeEnum.ADDITION;
        BigDecimal operandA = new BigDecimal("10");
        BigDecimal operandB = new BigDecimal("2");
        BigDecimal result = new BigDecimal("5");
        LocalDateTime timestamp = LocalDateTime.now();
        UserEntity user = new UserEntity();

        CalculatorOperation operation = new CalculatorOperation();
        operation.setId(id);
        operation.setOperation(operationType);
        operation.setOperandA(operandA);
        operation.setOperandB(operandB);
        operation.setResult(result);
        operation.setTimestamp(timestamp);
        operation.setUser(user);

        assertEquals(id, operation.getId());
        assertEquals(operationType, operation.getOperation());
        assertEquals(operandA, operation.getOperandA());
        assertEquals(operandB, operation.getOperandB());
        assertEquals(result, operation.getResult());
        assertEquals(timestamp, operation.getTimestamp());
        assertEquals(user, operation.getUser());
    }
}
