package com.marcelohofart.bank_api.services;

import com.marcelohofart.bank_api.exceptions.AccountNotFoundException;
import com.marcelohofart.bank_api.exceptions.InsufficientBalanceException;
import com.marcelohofart.bank_api.models.Account;
import com.marcelohofart.bank_api.models.Transfer;
import com.marcelohofart.bank_api.repositories.AccountRepository;
import com.marcelohofart.bank_api.repositories.TransferRepository;
import com.marcelohofart.bank_api.requests.TransferRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {

    @InjectMocks
    private TransferService transferService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransferRepository transferRepository;

    private UUID fromAccountId;
    private UUID toAccountId;
    private Account fromAccount;
    private Account toAccount;
    private TransferRequest transferRequest;

    @BeforeEach
    void setup() {
        fromAccountId = UUID.randomUUID();
        toAccountId = UUID.randomUUID();
        fromAccount = new Account();
        fromAccount.setBalance(BigDecimal.valueOf(100));
        toAccount = new Account();
        toAccount.setBalance(BigDecimal.valueOf(50));

        transferRequest = new TransferRequest();
        transferRequest.fromAccountId = fromAccountId;
        transferRequest.toAccountId = toAccountId;
        transferRequest.amount = BigDecimal.valueOf(20);
        transferRequest.description = "Pagamento de serviço";
    }

    @Test
    void testProcessTransfer_Successful() {
        // Cenário: as contas existem, a transferência é válida e o saldo é suficiente
        when(accountRepository.findByIdWithLock(fromAccountId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByIdWithLock(toAccountId)).thenReturn(Optional.of(toAccount));

        transferService.processTransferBetweenAccounts(transferRequest);

        assertEquals(BigDecimal.valueOf(80), fromAccount.getBalance());
        assertEquals(BigDecimal.valueOf(70), toAccount.getBalance());
        verify(transferRepository).save(any(Transfer.class));
    }

    @Test
    void testProcessTransfer_AccountNotFound_FromAccount() {
        // Cenário: a conta de origem não existe
        when(accountRepository.findByIdWithLock(fromAccountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () ->
                transferService.processTransferBetweenAccounts(transferRequest)
        );
        verify(transferRepository, never()).save(any());
    }

    @Test
    void testProcessTransfer_AccountNotFound_ToAccount() {
        // Cenário: a conta de destino não existe
        when(accountRepository.findByIdWithLock(fromAccountId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByIdWithLock(toAccountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () ->
                transferService.processTransferBetweenAccounts(transferRequest)
        );
        verify(transferRepository, never()).save(any());
    }

    @Test
    void testProcessTransfer_SameAccountTransfer() {
        // Cenário: tentativa de transferir para a mesma conta
        transferRequest.toAccountId = fromAccountId;

        transferService.processTransferBetweenAccounts(transferRequest);

        verify(accountRepository, never()).findByIdWithLock(any());
        verify(transferRepository, never()).save(any());
    }

    @Test
    void testProcessTransfer_InsufficientBalance() {
        // Cenário: saldo insuficiente na conta de origem
        transferRequest.amount = BigDecimal.valueOf(200);

        when(accountRepository.findByIdWithLock(fromAccountId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByIdWithLock(toAccountId)).thenReturn(Optional.of(toAccount));

        assertThrows(InsufficientBalanceException.class, () ->
                transferService.processTransferBetweenAccounts(transferRequest)
        );
        verify(transferRepository, never()).save(any());
    }
}
