package com.marcelohofart.bank_api.controllers;

import com.marcelohofart.bank_api.dtos.AccountDto;
import com.marcelohofart.bank_api.models.Account;
import com.marcelohofart.bank_api.models.Agency;
import com.marcelohofart.bank_api.models.Bank;
import com.marcelohofart.bank_api.models.Client;
import com.marcelohofart.bank_api.requests.TransactionRequest;
import com.marcelohofart.bank_api.requests.TransferRequest;
import com.marcelohofart.bank_api.services.AccountService;
import com.marcelohofart.bank_api.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;
    @PostMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<String> createTransactionsInAccount(
            @Valid @PathVariable UUID accountId,
            @Valid @RequestBody List<TransactionRequest> transactionRequestList
    ) {
        transactionService.processTransactions(accountId, transactionRequestList);
        return ResponseEntity.status(HttpStatus.CREATED).body("Transações realizadas com sucesso!.");
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
    @GetMapping("/accounts")
    public ResponseEntity<Page<AccountDto>> getAllAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int page_size
    ) {
        if (page < 0) {
            throw new IllegalArgumentException("O número da página não pode ser menor que zero.");
        }

        if (page_size > 50) {
            throw new IllegalArgumentException("O tamanho da página não pode ser maior que 50.");
        }

        Pageable pageable = PageRequest.of(page, page_size, Sort.by(Sort.Direction.ASC, "number"));

        Page<AccountDto> accounts = accountService.getAllAccounts(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(accounts);
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<String> getAccountDetails(
            @Valid @PathVariable UUID accountId
    ) {
        try {
            // Lógica para processar as transações usando o accountId e transactionRequest

            return ResponseEntity.status(HttpStatus.OK).body("test");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing transactions.");
        }
    }
}
