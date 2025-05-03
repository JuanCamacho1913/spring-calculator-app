package com.calculator.presentation.dto;

import com.calculator.util.OperationTypeEnum;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CalculationOperationRequest(

        OperationTypeEnum operation,

        @DecimalMax(value = "1000000", message = "The max range must be 1000000")
        @DecimalMin(value = "-1000000", message = "The min range must be -1000000")
        BigDecimal operandA,
        @DecimalMax(value = "1000000", message = "The max range must be 1000000")
        @DecimalMin(value = "-1000000", message = "The min range must be -1000000")
        BigDecimal operandB
) {
}
