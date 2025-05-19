package com.calculator.util;

import com.fasterxml.jackson.annotation.JsonIgnore;

public enum OperationTypeEnum {

    ADDITION,
    SUBTRACTION,
    MULTIPLICATION,
    DIVISION,
    POWER,
    SQUARE_ROOT,

    @JsonIgnore
    UNKNOWN
}
