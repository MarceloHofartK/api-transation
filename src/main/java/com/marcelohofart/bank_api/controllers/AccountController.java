package com.marcelohofart.bank_api.controllers;

import com.marcelohofart.bank_api.dtos.AccountDto;
import com.marcelohofart.bank_api.requests.TransactionRequest;
import com.marcelohofart.bank_api.requests.TransferRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class AccountController {

    @PostMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<String> createTransactionsInAccount(
            @Valid @PathVariable UUID accountId,
            @Valid @RequestBody TransactionRequest transactionRequest
    ) {
        try {
            // Lógica para processar as transações usando o accountId e transactionRequest


            return ResponseEntity.status(HttpStatus.CREATED).body("Transactions successfully processed.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing transactions.");
        }
    }

    @PostMapping("/accounts/transfer")
    public ResponseEntity<String> transferBetweenAccounts(
            @Valid @RequestBody TransferRequest transferRequest
    ) {
        try {
            // Lógica para processar as transações usando o accountId e transactionRequest


            return ResponseEntity.status(HttpStatus.CREATED).body("Transactions successfully processed.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing transactions.");
        }
    }
    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<String> getAccountDetails(
            @Valid @PathVariable UUID accountId
    ) {
        try {
            // Lógica para processar as transações usando o accountId e transactionRequest

            return ResponseEntity.status(HttpStatus.CREATED).body("test");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing transactions.");
        }
    }
}
