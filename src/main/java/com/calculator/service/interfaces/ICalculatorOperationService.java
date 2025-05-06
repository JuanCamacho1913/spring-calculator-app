package com.calculator.service.interfaces;

import com.calculator.presentation.dto.CalculationOperationRequest;
import com.calculator.presentation.dto.CalculationOperationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ICalculatorOperationService {

    Page<CalculationOperationResponse> findAll(Pageable pageable, String operationType, String startDate, String endDate);
    CalculationOperationResponse findById(UUID id);
    CalculationOperationResponse saveCalculation(CalculationOperationRequest calculationOperationRequest);
    String delete(UUID uuid);
}
