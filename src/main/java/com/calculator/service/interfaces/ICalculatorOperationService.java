package com.calculator.service.interfaces;

import com.calculator.presentation.dto.CalculationOperationRequest;
import com.calculator.presentation.dto.CalculationOperationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ICalculatorOperationService {

    Page<CalculationOperationResponse> findAll(Pageable pageable);
    CalculationOperationResponse findById(UUID id);
    CalculationOperationResponse saveCalculation(CalculationOperationRequest calculationOperationRequest);
    String delete(UUID uuid);
}
