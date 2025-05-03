package com.calculator.service.impl;

import com.calculator.exception.error.ArithmeticOperationException;
import com.calculator.exception.error.ElementNotFoundException;
import com.calculator.mapper.ICalculatorOperatorMapper;
import com.calculator.persistence.entity.CalculatorOperation;
import com.calculator.persistence.repository.ICalculatorRepository;
import com.calculator.presentation.dto.CalculationOperationRequest;
import com.calculator.presentation.dto.CalculationOperationResponse;
import com.calculator.service.interfaces.ICalculatorOperationService;
import com.calculator.util.OperationTypeEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CalculatorOperationServiceImpl implements ICalculatorOperationService {

    private ICalculatorRepository calculatorRepository;
    private ICalculatorOperatorMapper calculatorOperatorMapper;

    @Override
    public List<CalculationOperationResponse> findAll() {
        List<CalculatorOperation> calculatorOperationList = this.calculatorRepository.findAll();

        return this.calculatorOperatorMapper.toResponseList(calculatorOperationList);
    }

    @Override
    public CalculationOperationResponse findById(UUID id) {
        CalculatorOperation calculatorOperation = this.calculatorRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Operation whit " + id + " does not exist"));

        return this.calculatorOperatorMapper.toResponse(calculatorOperation);
    }

    @Override
    public CalculationOperationResponse save(CalculationOperationRequest request) {
        BigDecimal result;

        switch (request.operation()){
            case ADDITION:
                result = request.operandA().add(request.operandB());
                break;
            case SUBTRACTION:
                result = request.operandA().subtract(request.operandB());
                break;
            case MULTIPLICATION:
                result = request.operandA().multiply(request.operandB());
                break;
            case DIVISION:
                result = request.operandA().divide(request.operandB());
                break;
            case POWER:
                result = request.operandA().pow(request.operandB().intValueExact());
                break;
            case SQUARE_ROOT:
                double operandA = request.operandA().doubleValue();
                result = BigDecimal.valueOf(Math.sqrt(operandA)).round(new MathContext(7));
            default:
                throw new ArithmeticOperationException("Arithmetic operation don't exist");
        }

        CalculatorOperation calculatorOperation = this.calculatorOperatorMapper.toEntity(request);
        calculatorOperation.setResult(result);
        CalculatorOperation calculatorOperationSaved = this.calculatorRepository.save(calculatorOperation);
        return this.calculatorOperatorMapper.toResponse(calculatorOperationSaved);
    }
}
