package com.marcelohofart.bank_api.exceptions;

public class InvalidTransferRequestException extends RuntimeException{
    public InvalidTransferRequestException() {
        super("O objeto de transferência estava em um formato incorreto.");
    }
    public InvalidTransferRequestException(String message) {
        super(message);
    }
}
