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

        // Verifica se o saldo foi alterado corretamente nas duas contas
        assertEquals(BigDecimal.valueOf(80), fromAccount.getBalance()); // Saldo da conta de origem após o débito
        assertEquals(BigDecimal.valueOf(70), toAccount.getBalance()); // Saldo da conta de destino após o crédito
        verify(transferRepository).save(any(Transfer.class)); // Verifica se o objeto Transfer foi salvo
    }

    @Test
    void testProcessTransfer_AccountNotFound_FromAccount() {
        // Cenário: a conta de origem não existe
        when(accountRepository.findByIdWithLock(fromAccountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () ->
                transferService.processTransferBetweenAccounts(transferRequest)
        );
        verify(transferRepository, never()).save(any()); // Não deve salvar nenhuma transferência
    }

    @Test
    void testProcessTransfer_AccountNotFound_ToAccount() {
        // Cenário: a conta de destino não existe
        when(accountRepository.findByIdWithLock(fromAccountId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByIdWithLock(toAccountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () ->
                transferService.processTransferBetweenAccounts(transferRequest)
        );
        verify(transferRepository, never()).save(any()); // Não deve salvar nenhuma transferência
    }

    @Test
    void testProcessTransfer_SameAccountTransfer() {
        // Cenário: tentativa de transferir para a mesma conta
        transferRequest.toAccountId = fromAccountId; // Definindo o mesmo ID para origem e destino

        transferService.processTransferBetweenAccounts(transferRequest);

        verify(accountRepository, never()).findByIdWithLock(any()); // Não deve fazer nenhuma operação de busca
        verify(transferRepository, never()).save(any()); // Não deve salvar nenhuma transferência
    }

    @Test
    void testProcessTransfer_InsufficientBalance() {
        // Cenário: saldo insuficiente na conta de origem
        transferRequest.amount = BigDecimal.valueOf(200); // Tentando transferir mais do que o saldo

        when(accountRepository.findByIdWithLock(fromAccountId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByIdWithLock(toAccountId)).thenReturn(Optional.of(toAccount));

        assertThrows(InsufficientBalanceException.class, () ->
                transferService.processTransferBetweenAccounts(transferRequest)
        );
        verify(transferRepository, never()).save(any()); // Não deve salvar nenhuma transferência
    }
}
