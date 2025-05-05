package com.calculator.mapper;


import com.calculator.persistence.entity.CalculatorOperation;
import com.calculator.presentation.dto.CalculationOperationRequest;
import com.calculator.presentation.dto.CalculationOperationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ICalculatorOperatorMapper {

    CalculatorOperation toEntity(CalculationOperationRequest calculationOperationRequest);

    @Mapping(source = "user.id", target = "userId")
    CalculationOperationResponse toResponse(CalculatorOperation calculatorOperation);

    List<CalculationOperationResponse> toResponseList(List<CalculatorOperation> calculationOperation);
}
