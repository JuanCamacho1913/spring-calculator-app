package com.calculator.presentation.dto;

import com.calculator.util.OperationTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CalculationOperationResponse(
        UUID id,
        OperationTypeEnum operation,
        BigDecimal operandA,
        BigDecimal operandB,
        BigDecimal result,
        LocalDateTime timestamp,
        UUID userId
) {
}
