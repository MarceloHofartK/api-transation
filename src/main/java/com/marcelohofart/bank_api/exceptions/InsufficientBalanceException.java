package com.marcelohofart.bank_api.exceptions;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException() {
        super("Saldo insuficiente na conta para realizar a transação.");
    }
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
