package com.marcelohofart.bank_api.services;

import com.marcelohofart.bank_api.enums.TransactionType;
import com.marcelohofart.bank_api.exceptions.AccountNotFoundException;
import com.marcelohofart.bank_api.models.Account;
import com.marcelohofart.bank_api.models.Transaction;
import com.marcelohofart.bank_api.models.Transfer;
import com.marcelohofart.bank_api.repositories.AccountRepository;
import com.marcelohofart.bank_api.repositories.TransactionRepository;
import com.marcelohofart.bank_api.repositories.TransferRepository;
import com.marcelohofart.bank_api.requests.TransactionRequest;
import com.marcelohofart.bank_api.requests.TransferRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class TransferService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransferRepository transferRepository;

    @Transactional
    public void processTransferBetweenAccounts(TransferRequest transferRequest){
        if(transferRequest.toAccountId == transferRequest.fromAccountId){
            return;
        }
        transferRequest.validate();

        Account fromAccount = accountRepository.findByIdWithLock(transferRequest.fromAccountId)
                .orElseThrow(() -> new AccountNotFoundException("Conta de origem não encontrada"));

        Account toAccount = accountRepository.findByIdWithLock(transferRequest.toAccountId)
                .orElseThrow(() -> new AccountNotFoundException("Conta de destino não encontrada"));

        fromAccount.applyTransaction(TransactionType.DEBIT, transferRequest.amount);
        toAccount.applyTransaction(TransactionType.CREDIT, transferRequest.amount);

        Transfer transfer = new Transfer(transferRequest.amount, transferRequest.description, fromAccount, toAccount);
        transferRepository.save(transfer);
    }
}
