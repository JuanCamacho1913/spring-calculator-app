package com.calculator.presentation.controller;

import com.calculator.presentation.dto.CalculationOperationRequest;
import com.calculator.presentation.dto.CalculationOperationResponse;
import com.calculator.service.interfaces.ICalculatorOperationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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

    @GetMapping("/findAll")
    public ResponseEntity<List<CalculationOperationResponse>> findAll(){
        List<CalculationOperationResponse> calculationOperationResponseList = this.calculatorOperationService.findAll();
        return new ResponseEntity<>(calculationOperationResponseList, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<CalculationOperationResponse> findById(@PathVariable UUID id){
        CalculationOperationResponse calculationOperationResponse = this.calculatorOperationService.findById(id);
        return new ResponseEntity<>(calculationOperationResponse, HttpStatus.OK);
    }

    @PostMapping("/calculate")
    public ResponseEntity<CalculationOperationResponse> saveCalculation(@RequestBody @Valid CalculationOperationRequest request){
        CalculationOperationResponse calculationOperationResponse = this.calculatorOperationService.saveCalculation(request);
        return new ResponseEntity<>(calculationOperationResponse, HttpStatus.CREATED);
    }
}
