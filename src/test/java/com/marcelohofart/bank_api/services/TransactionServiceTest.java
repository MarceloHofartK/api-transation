package com.marcelohofart.bank_api.services;

import com.marcelohofart.bank_api.exceptions.AccountNotFoundException;
import com.marcelohofart.bank_api.exceptions.InsufficientBalanceException;
import com.marcelohofart.bank_api.exceptions.InvalidTransactionRequestException;
import com.marcelohofart.bank_api.models.Account;
import com.marcelohofart.bank_api.models.Transaction;
import com.marcelohofart.bank_api.repositories.AccountRepository;
import com.marcelohofart.bank_api.repositories.TransactionRepository;
import org.junit.jupiter.api.Test;
import com.marcelohofart.bank_api.requests.TransactionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    private UUID accountId;
    private Account account;

    @BeforeEach
    void setup() {
        accountId = UUID.randomUUID();
        account = new Account();
        account.setBalance(BigDecimal.valueOf(100));
    }

    @Test
    void testProcessTransactions_SuccessfulCredit() {
        // Cenário: Transação de crédito bem-sucedida
        TransactionRequest req = new TransactionRequest();
        req.transactionType = "CREDIT";
        req.amount = BigDecimal.valueOf(50);
        req.description = "Depositei dinheiro na conta";

        when(accountRepository.findByIdWithLock(accountId)).thenReturn(Optional.of(account));

        transactionService.processTransactions(accountId, List.of(req));

        assertEquals(BigDecimal.valueOf(150), account.getBalance());
        verify(transactionRepository).save(any(Transaction.class)); // Verifica se a transação foi salva
    }

    @Test
    void testProcessTransactions_SuccessfulDebit() {
        // Cenário: Transação de débito bem-sucedida
        TransactionRequest req = new TransactionRequest();
        req.transactionType = "DEBIT";
        req.amount = BigDecimal.valueOf(40);
        req.description = "Comprei um mc donalds";

        when(accountRepository.findByIdWithLock(accountId)).thenReturn(Optional.of(account));

        transactionService.processTransactions(accountId, List.of(req));

        assertEquals(BigDecimal.valueOf(60), account.getBalance());
        verify(transactionRepository).save(any(Transaction.class)); // Verifica se a transação foi salva
    }

    @Test
    void testProcessTransactions_SuccessfulMultipleTransactions() {
        // Cenário: Processando múltiplas transações com sucesso (crédito + débito)
        TransactionRequest credit = new TransactionRequest();
        credit.transactionType = "CREDIT";
        credit.amount = BigDecimal.valueOf(200);
        credit.description = "Salário";

        TransactionRequest debit = new TransactionRequest();
        debit.transactionType = "DEBIT";
        debit.amount = BigDecimal.valueOf(50);
        debit.description = "Supermercado";

        when(accountRepository.findByIdWithLock(accountId)).thenReturn(Optional.of(account));

        transactionService.processTransactions(accountId, List.of(credit, debit));

        // 100 inicial + 200 - 50 = 250
        assertEquals(BigDecimal.valueOf(250), account.getBalance());
        verify(transactionRepository, times(2)).save(any(Transaction.class)); // Verifica se as duas transações foram salvas
    }

    @Test
    void testProcessTransactions_InsufficientBalanceMultipleTransactions() {
        // Cenário: Saldo insuficiente para processar múltiplas transações
        TransactionRequest credit = new TransactionRequest();
        credit.transactionType = "CREDIT";
        credit.amount = BigDecimal.valueOf(200);
        credit.description = "Salário";

        TransactionRequest debit = new TransactionRequest();
        debit.transactionType = "DEBIT";
        debit.amount = BigDecimal.valueOf(400);
        debit.description = "Supermercado";

        when(accountRepository.findByIdWithLock(accountId)).thenReturn(Optional.of(account));

        // 100 inicial + 200 - 400 = -100 (InsufficientBalanceException)
        assertThrows(InsufficientBalanceException.class, () ->
                transactionService.processTransactions(accountId, List.of(credit, debit))
        );
    }

    @Test
    void testProcessTransactions_InsufficientBalance() {
        // Cenário: Saldo insuficiente para realizar uma transação de débito
        TransactionRequest req = new TransactionRequest();
        req.transactionType = "DEBIT";
        req.amount = BigDecimal.valueOf(200);
        req.description = "Comprei 1 kg de café";

        when(accountRepository.findByIdWithLock(accountId)).thenReturn(Optional.of(account));

        assertThrows(InsufficientBalanceException.class, () ->
                transactionService.processTransactions(accountId, List.of(req))
        );
        verify(transactionRepository, never()).save(any()); // Não deve salvar nenhuma transação devido ao erro de saldo insuficiente
    }

    @Test
    void testProcessTransactions_AccountNotFound() {
        // Cenário: Conta não encontrada no banco de dados
        when(accountRepository.findByIdWithLock(accountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () ->
                transactionService.processTransactions(accountId, List.of(new TransactionRequest()))
        );
    }

    @Test
    void testProcessTransactions_EmptyTransactionList() {
        // Cenário: Lista de transações vazia (nenhuma transação a ser processada)
        transactionService.processTransactions(accountId, Collections.emptyList());
        verifyNoInteractions(accountRepository); // Nenhuma interação com o repositório de conta
    }

    @Test
    void testProcessTransactions_InvalidTransactionType() {
        // Cenário: Tipo de transação inválido
        TransactionRequest req = new TransactionRequest();
        req.transactionType = "INVALID";
        req.amount = BigDecimal.valueOf(10);

        assertThrows(InvalidTransactionRequestException.class, req::validate); // Deve lançar uma exceção devido ao tipo de transação inválido
    }


}
