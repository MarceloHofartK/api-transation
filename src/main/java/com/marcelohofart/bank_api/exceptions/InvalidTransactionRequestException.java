package com.marcelohofart.bank_api.exceptions;

public class InvalidTransactionRequestException extends RuntimeException{
    public InvalidTransactionRequestException() {
        super("O objeto de transação estava em um formato incorreto.");
    }
    public InvalidTransactionRequestException(String message) {
        super(message);
    }
}
