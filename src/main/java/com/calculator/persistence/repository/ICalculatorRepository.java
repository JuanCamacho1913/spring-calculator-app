package com.calculator.persistence.repository;

import com.calculator.persistence.entity.CalculatorOperation;
import com.calculator.util.OperationTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface ICalculatorRepository extends JpaRepository<CalculatorOperation, UUID> {

    Page<CalculatorOperation> findByOperationAndTimestampBetween(
            OperationTypeEnum operation,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );

    Page<CalculatorOperation> findByTimestampBetween(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );
}
