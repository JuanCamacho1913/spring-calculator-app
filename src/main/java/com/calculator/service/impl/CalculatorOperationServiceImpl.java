package com.calculator.service.impl;

import com.calculator.exception.error.ArithmeticOperationException;
import com.calculator.exception.error.ElementNotFoundException;
import com.calculator.mapper.ICalculatorOperatorMapper;
import com.calculator.persistence.entity.CalculatorOperation;
import com.calculator.persistence.entity.UserEntity;
import com.calculator.persistence.repository.ICalculatorRepository;
import com.calculator.presentation.dto.CalculationOperationRequest;
import com.calculator.presentation.dto.CalculationOperationResponse;
import com.calculator.service.interfaces.ICalculatorOperationService;
import com.calculator.util.OperationTypeEnum;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CalculatorOperationServiceImpl implements ICalculatorOperationService {

    private ICalculatorRepository calculatorRepository;
    private ICalculatorOperatorMapper calculatorOperatorMapper;
    private UserDetailServiceImpl userDetailServiceImpl;

    @Override
    public Page<CalculationOperationResponse> findAll(Pageable pageable, String operationType, String startDate, String endDate) {

        if (!StringUtils.isBlank(operationType) && StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate)) {
           Page<CalculatorOperation> calculatorOperations = this.calculatorRepository.findByOperation(OperationTypeEnum.valueOf(operationType), pageable);
           return calculatorOperations.map(calculatorOperation -> this.calculatorOperatorMapper.toResponse(calculatorOperation));
        }

        if (StringUtils.isBlank(operationType) && !StringUtils.isBlank(startDate) && !StringUtils.isBlank(endDate)) {
            LocalDateTime start;
            LocalDateTime end;

            try {
                start = LocalDateTime.parse(startDate);
                end = LocalDateTime.parse(endDate);
                Page<CalculatorOperation> calculatorOperationTimes = this.calculatorRepository.findByTimestampBetween(start, end, pageable);
                return calculatorOperationTimes.map(calculatorOperation -> this.calculatorOperatorMapper.toResponse(calculatorOperation));
            }catch (DateTimeParseException dateTimeParseException){
                throw new RuntimeException("Error parsing date" + dateTimeParseException.getMessage());
            }

        }

        Page<CalculatorOperation> calculatorOperation = this.calculatorRepository.findAll(pageable);
        return calculatorOperation.map(calculatorOperation1 -> this.calculatorOperatorMapper.toResponse(calculatorOperation1));
    }

    @Override
    public CalculationOperationResponse findById(UUID id) {
        CalculatorOperation calculatorOperation = this.calculatorRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Operation whit " + id + " does not exist"));

        return this.calculatorOperatorMapper.toResponse(calculatorOperation);
    }

    @Override
    public CalculationOperationResponse saveCalculation(CalculationOperationRequest request) {

        BigDecimal result;

        switch (request.operation()){
            case ADDITION:
                result = request.operandA().add(request.operandB()).setScale(2, RoundingMode.HALF_UP);
                break;
            case SUBTRACTION:
                result = request.operandA().subtract(request.operandB()).setScale(2, RoundingMode.HALF_UP);
                break;
            case MULTIPLICATION:
                result = request.operandA().multiply(request.operandB()).setScale(2, RoundingMode.HALF_UP);
                break;
            case DIVISION:
                if (BigDecimal.ZERO.compareTo(request.operandB()) == 0) {
                    throw new ArithmeticException("Cannot be divided by zero.");
                }
                result = request.operandA().divide(request.operandB(), MathContext.DECIMAL128).setScale(2, RoundingMode.HALF_UP);
                break;
            case POWER:
                result = request.operandA().pow(request.operandB().intValueExact()).setScale(2, RoundingMode.HALF_UP);
                break;
            case SQUARE_ROOT:
                if (request.operandA().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new ArithmeticException("Cannot calculate square root of 0 or negative number.");
                }
                double operandA = request.operandA().doubleValue();
                result = BigDecimal.valueOf(Math.sqrt(operandA)).round(new MathContext(7)).setScale(2, RoundingMode.HALF_UP);
                break;
            default:
                throw new ArithmeticOperationException("Arithmetic operation don't exist");
        }
        UserEntity user = this.userDetailServiceImpl.getCurrentUsername();
        CalculatorOperation calculatorOperation = this.calculatorOperatorMapper.toEntity(request);
        calculatorOperation.setResult(result);
        calculatorOperation.setUser(user);
        CalculatorOperation calculatorOperationSaved = this.calculatorRepository.save(calculatorOperation);
        return this.calculatorOperatorMapper.toResponse(calculatorOperationSaved);
    }

    @Override
    public String delete(UUID id) {
        CalculatorOperation calculatorOperation = this.calculatorRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Operation whit " + id + " does not exist"));

        this.calculatorRepository.delete(calculatorOperation);

        return "Operation delete successful";
    }
}
