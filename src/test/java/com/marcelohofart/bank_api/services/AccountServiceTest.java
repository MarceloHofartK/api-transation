package com.marcelohofart.bank_api.services;

import com.marcelohofart.bank_api.dtos.AccountDto;
import com.marcelohofart.bank_api.models.Account;
import com.marcelohofart.bank_api.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    private UUID accountId;
    private Account account;

    @BeforeEach
    void setup() {
        accountId = UUID.randomUUID();
        account = new Account();
        account.setId(accountId);
        account.setNumber("12345");
        account.setBalance(BigDecimal.valueOf(100));
    }

    @Test
    void testGetAccountDetailsById_Found() {
        // Cenário: Conta encontrada ao buscar por ID
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        Optional<AccountDto> accountDto = accountService.getAccountDetailsById(accountId);

        assertTrue(accountDto.isPresent());
        assertEquals(accountId, accountDto.get().getId());
        assertEquals("12345", accountDto.get().getNumber());
        assertEquals(BigDecimal.valueOf(100), accountDto.get().getBalance());
    }

    @Test
    void testGetAccountDetailsById_NotFound() {
        // Cenário: Conta não encontrada ao buscar por ID
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        Optional<AccountDto> accountDto = accountService.getAccountDetailsById(accountId);

        assertFalse(accountDto.isPresent());
    }

    @Test
    void testGetAllAccounts_ValidPagination() {
        // Cenário: Obter todas as contas com paginação válida
        int page = 0;
        int size = 10;

        List<AccountDto> accountList = List.of(new AccountDto(account.getId(), account.getNumber(), account.getBalance()));
        Pageable pageable = PageRequest.of(page, size);
        Page<AccountDto> accountPage = new PageImpl<>(accountList, pageable, accountList.size());

        when(accountRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(account), pageable, accountList.size()));

        Page<AccountDto> result = accountService.getAllAccounts(pageable);

        assertEquals(accountPage.getTotalElements(), result.getTotalElements());
        assertEquals(accountPage.getContent().size(), result.getContent().size());
        assertEquals(accountPage.getContent().get(0).getId(), result.getContent().get(0).getId());
    }

    @Test
    void testGetAllAccounts_EmptyPage() {
        // Cenário: Obter todas as contas quando não há contas
        int page = 0;
        int size = 10;

        List<Account> accountList = List.of();
        Pageable pageable = PageRequest.of(page, size);

        Page<Account> accountPage = new PageImpl<>(accountList, pageable, accountList.size());

        when(accountRepository.findAll(pageable)).thenReturn(accountPage);

        Page<AccountDto> result = accountService.getAllAccounts(pageable);

        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
    }

}
