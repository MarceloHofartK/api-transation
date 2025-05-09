package com.marcelohofart.bank_api.services;

import com.marcelohofart.bank_api.enums.TransactionType;
import com.marcelohofart.bank_api.exceptions.AccountNotFoundException;
import com.marcelohofart.bank_api.models.Account;
import com.marcelohofart.bank_api.models.Transaction;
import com.marcelohofart.bank_api.repositories.AccountRepository;
import com.marcelohofart.bank_api.repositories.TransactionRepository;
import com.marcelohofart.bank_api.requests.TransactionRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public void processTransactions(UUID accountId, List<TransactionRequest> transactionRequestList){
        if (transactionRequestList.isEmpty()) return;

        Account account = accountRepository.findByIdWithLock(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Conta não encontrada"));

        for(TransactionRequest transactionRequest : transactionRequestList) {
            transactionRequest.validate(); // serve para validar a transação, caso um dado esteja errado gera exception

            BigDecimal balanceBefore = account.getBalance();
            account.applyTransaction(TransactionType.fromString(transactionRequest.transactionType),
                    transactionRequest.amount);

            BigDecimal balanceAfter = account.getBalance();

            Transaction transaction = new Transaction(TransactionType.fromString(transactionRequest.transactionType),
                    transactionRequest.amount, transactionRequest.description, account,
                    balanceBefore, balanceAfter);
            transactionRepository.save(transaction);
        }
    }
}
