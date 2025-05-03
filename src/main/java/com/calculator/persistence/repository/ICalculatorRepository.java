package com.calculator.persistence.repository;

import com.calculator.persistence.entity.CalculatorOperation;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ICalculatorRepository extends ListCrudRepository<CalculatorOperation, UUID> {
}
