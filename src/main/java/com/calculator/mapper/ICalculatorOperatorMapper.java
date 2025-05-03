package com.calculator.mapper;


import com.calculator.persistence.entity.CalculatorOperation;
import com.calculator.presentation.dto.CalculationOperationRequest;
import com.calculator.presentation.dto.CalculationOperationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ICalculatorOperatorMapper {

    ICalculatorOperatorMapper INSTANCE = Mappers.getMapper(ICalculatorOperatorMapper.class);

    CalculatorOperation toEntity(CalculationOperationRequest calculationOperationRequest);

    CalculationOperationResponse toResponse(CalculatorOperation calculatorOperation);

    List<CalculationOperationResponse> toResponseList(List<CalculatorOperation> calculationOperation);
}
