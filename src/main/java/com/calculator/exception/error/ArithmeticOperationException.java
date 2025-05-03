package com.calculator.exception.error;

public class ArithmeticOperationException extends RuntimeException{

    public ArithmeticOperationException() {
        super();
    }

    public ArithmeticOperationException(String message) {
        super(message);
    }

    public ArithmeticOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArithmeticOperationException(Throwable cause) {
        super(cause);
    }
}
