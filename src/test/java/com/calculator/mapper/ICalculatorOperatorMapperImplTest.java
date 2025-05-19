package com.calculator.mapper;

import com.calculator.persistence.entity.CalculatorOperation;
import com.calculator.persistence.entity.UserEntity;
import com.calculator.presentation.dto.CalculationOperationRequest;
import com.calculator.presentation.dto.CalculationOperationResponse;
import com.calculator.util.OperationTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ICalculatorOperatorMapperImplTest {

    @InjectMocks
    private ICalculatorOperatorMapperImpl mapper;

    @Test
    void toEntityTest() {
        CalculationOperationRequest request = new CalculationOperationRequest(
                OperationTypeEnum.SUBTRACTION,
                new BigDecimal("8"),
                new BigDecimal("3")
        );

        CalculatorOperation entity = mapper.toEntity(request);

        assertThat(entity).isNotNull();
        assertThat(entity.getOperation()).isEqualTo(OperationTypeEnum.SUBTRACTION);
        assertThat(entity.getOperandA()).isEqualByComparingTo("8");
        assertThat(entity.getOperandB()).isEqualByComparingTo("3");
    }

    @Test
    void toResponseTest() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        UserEntity user = new UserEntity();
        user.setId(userId);

        CalculatorOperation entity = new CalculatorOperation();
        entity.setId(id);
        entity.setOperation(OperationTypeEnum.DIVISION);
        entity.setOperandA(new BigDecimal("20"));
        entity.setOperandB(new BigDecimal("5"));
        entity.setResult(new BigDecimal("4"));
        entity.setTimestamp(now);
        entity.setUser(user);

        CalculationOperationResponse response = mapper.toResponse(entity);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.operation()).isEqualTo(OperationTypeEnum.DIVISION);
        assertThat(response.operandA()).isEqualByComparingTo("20");
        assertThat(response.operandB()).isEqualByComparingTo("5");
        assertThat(response.result()).isEqualByComparingTo("4");
        assertThat(response.timestamp()).isEqualTo(now);
        assertThat(response.userId()).isEqualTo(userId);
    }

    @Test
    void toResponseListTest() {
        UUID userId = UUID.randomUUID();
        UserEntity user = new UserEntity();
        user.setId(userId);

        CalculatorOperation entity1 = new CalculatorOperation();
        entity1.setId(UUID.randomUUID());
        entity1.setOperation(OperationTypeEnum.MULTIPLICATION);
        entity1.setOperandA(BigDecimal.ONE);
        entity1.setOperandB(BigDecimal.TEN);
        entity1.setResult(BigDecimal.TEN);
        entity1.setTimestamp(LocalDateTime.now());
        entity1.setUser(user);

        CalculatorOperation entity2 = new CalculatorOperation();
        entity2.setId(UUID.randomUUID());
        entity2.setOperation(OperationTypeEnum.ADDITION);
        entity2.setOperandA(new BigDecimal("2"));
        entity2.setOperandB(new BigDecimal("3"));
        entity2.setResult(new BigDecimal("5"));
        entity2.setTimestamp(LocalDateTime.now());
        entity2.setUser(user);

        List<CalculatorOperation> entityList = List.of(entity1, entity2);

        List<CalculationOperationResponse> responseList = mapper.toResponseList(entityList);

        assertThat(responseList).hasSize(2);
        assertThat(responseList.get(0).operation()).isEqualTo(OperationTypeEnum.MULTIPLICATION);
        assertThat(responseList.get(1).operation()).isEqualTo(OperationTypeEnum.ADDITION);
    }

    @Test
    void toResponseWhenUserIsNullTest() {
        CalculatorOperation entity = new CalculatorOperation();
        entity.setId(UUID.randomUUID());
        entity.setOperation(OperationTypeEnum.SUBTRACTION);
        entity.setOperandA(new BigDecimal("10"));
        entity.setOperandB(new BigDecimal("4"));
        entity.setResult(new BigDecimal("6"));
        entity.setTimestamp(LocalDateTime.now());
        entity.setUser(null);

        CalculationOperationResponse response = mapper.toResponse(entity);

        assertThat(response).isNotNull();
        assertThat(response.userId()).isNull();
        assertThat(response.operation()).isEqualTo(OperationTypeEnum.SUBTRACTION);
    }

    @Test
    void toEntityWhenRequestIsNullTest() {
        assertThat(mapper.toEntity(null)).isNull();
    }

    @Test
    void toResponseWhenEntityIsNullTest() {
        assertThat(mapper.toResponse(null)).isNull();
    }

    @Test
    void toResponseListWhenListIsNullTest() {
        assertThat(mapper.toResponseList(null)).isNull();
    }
}
