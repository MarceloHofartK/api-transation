package com.marcelohofart.bank_api.infra;

import com.marcelohofart.bank_api.exceptions.*;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Hidden
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(InsufficientBalanceException.class)
    private ResponseEntity<String> insufficientBalanceHandler(InsufficientBalanceException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
    @ExceptionHandler(AccountNotFoundException.class)
    private ResponseEntity<String> AccountNotFoundHandler(AccountNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<String> IllegalArgumentHandler(IllegalArgumentException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
    @ExceptionHandler(InvalidTransactionRequestException.class)
    private ResponseEntity<String> InvalidTransactionRequestHandler(InvalidTransactionRequestException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
    @ExceptionHandler(InvalidTransferRequestException.class)
    private ResponseEntity<String> IInvalidTransferRequestHandler(InvalidTransferRequestException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
