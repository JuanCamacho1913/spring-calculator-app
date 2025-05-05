package com.calculator.mapper;

import com.calculator.persistence.entity.CalculatorOperation;
import com.calculator.persistence.entity.UserEntity;
import com.calculator.presentation.dto.CalculationOperationRequest;
import com.calculator.presentation.dto.CalculationOperationResponse;
import com.calculator.util.OperationTypeEnum;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-05T14:10:11-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.8 (Oracle Corporation)"
)
@Component
public class ICalculatorOperatorMapperImpl implements ICalculatorOperatorMapper {

    @Override
    public CalculatorOperation toEntity(CalculationOperationRequest calculationOperationRequest) {
        if ( calculationOperationRequest == null ) {
            return null;
        }

        CalculatorOperation calculatorOperation = new CalculatorOperation();

        calculatorOperation.setOperation( calculationOperationRequest.operation() );
        calculatorOperation.setOperandA( calculationOperationRequest.operandA() );
        calculatorOperation.setOperandB( calculationOperationRequest.operandB() );

        return calculatorOperation;
    }

    @Override
    public CalculationOperationResponse toResponse(CalculatorOperation calculatorOperation) {
        if ( calculatorOperation == null ) {
            return null;
        }

        UUID userId = null;
        UUID id = null;
        OperationTypeEnum operation = null;
        BigDecimal operandA = null;
        BigDecimal operandB = null;
        BigDecimal result = null;
        LocalDateTime timestamp = null;

        userId = calculatorOperationUserId( calculatorOperation );
        id = calculatorOperation.getId();
        operation = calculatorOperation.getOperation();
        operandA = calculatorOperation.getOperandA();
        operandB = calculatorOperation.getOperandB();
        result = calculatorOperation.getResult();
        timestamp = calculatorOperation.getTimestamp();

        CalculationOperationResponse calculationOperationResponse = new CalculationOperationResponse( id, operation, operandA, operandB, result, timestamp, userId );

        return calculationOperationResponse;
    }

    @Override
    public List<CalculationOperationResponse> toResponseList(List<CalculatorOperation> calculationOperation) {
        if ( calculationOperation == null ) {
            return null;
        }

        List<CalculationOperationResponse> list = new ArrayList<CalculationOperationResponse>( calculationOperation.size() );
        for ( CalculatorOperation calculatorOperation : calculationOperation ) {
            list.add( toResponse( calculatorOperation ) );
        }

        return list;
    }

    private UUID calculatorOperationUserId(CalculatorOperation calculatorOperation) {
        UserEntity user = calculatorOperation.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getId();
    }
}
