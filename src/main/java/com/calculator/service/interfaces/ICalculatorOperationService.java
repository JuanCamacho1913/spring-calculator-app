package com.calculator.service.interfaces;

import com.calculator.presentation.dto.CalculationOperationRequest;
import com.calculator.presentation.dto.CalculationOperationResponse;

import java.util.List;
import java.util.UUID;

public interface ICalculatorOperationService {

    List<CalculationOperationResponse> findAll();
    CalculationOperationResponse findById(UUID id);
    CalculationOperationResponse saveCalculation(CalculationOperationRequest calculationOperationRequest);
}
