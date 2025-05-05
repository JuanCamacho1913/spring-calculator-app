package com.calculator.presentation.controller;

import com.calculator.presentation.dto.CalculationOperationRequest;
import com.calculator.presentation.dto.CalculationOperationResponse;
import com.calculator.service.interfaces.ICalculatorOperationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class CalculatorOperationController {

    private ICalculatorOperationService calculatorOperationService;

    @GetMapping("/history")
    public ResponseEntity<Page<CalculationOperationResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").ascending());
        Page<CalculationOperationResponse> calculationOperationResponseList =
                this.calculatorOperationService.findAll(pageable, operationType, startDate, endDate);
        return new ResponseEntity<>(calculationOperationResponseList, HttpStatus.OK);
    }

    @GetMapping("/history/{id}")
    public ResponseEntity<CalculationOperationResponse> findById(@PathVariable UUID id){
        CalculationOperationResponse calculationOperationResponse = this.calculatorOperationService.findById(id);
        return new ResponseEntity<>(calculationOperationResponse, HttpStatus.OK);
    }

    @PostMapping("/calculate")
    public ResponseEntity<CalculationOperationResponse> saveCalculation(@RequestBody @Valid CalculationOperationRequest request){
        CalculationOperationResponse calculationOperationResponse = this.calculatorOperationService.saveCalculation(request);
        return new ResponseEntity<>(calculationOperationResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/history/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id){
        String result = this.calculatorOperationService.delete(id);
        return ResponseEntity.ok(result);
    }
}
