package com.calculator.persistence.entity;

import com.calculator.util.OperationTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "calculator_operation")
public class CalculatorOperation {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private OperationTypeEnum operation;

    @Column(name = "operand_A", precision = 7, scale = 2, nullable = false)
    private BigDecimal operandA;
    @Column(name = "operand_B", precision = 7, scale = 2, nullable = false)
    private BigDecimal operandB;
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal result;
    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @PrePersist
    public void prePersist(){
        if (id == null){
            this.id = UUID.randomUUID();
        }

        this.timestamp = LocalDateTime.now();
    }
}
