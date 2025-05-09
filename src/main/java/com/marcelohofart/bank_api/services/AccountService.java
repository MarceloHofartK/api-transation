package com.marcelohofart.bank_api.services;

import com.marcelohofart.bank_api.dtos.AccountDto;
import com.marcelohofart.bank_api.models.Account;
import com.marcelohofart.bank_api.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Optional<AccountDto> getAccountDetailsById(UUID accountId){
        return accountRepository.findById(accountId)
                .map(account -> new AccountDto(account.getId(), account.getNumber(), account.getBalance()));

    }
    public Page<AccountDto> getAllAccounts(Pageable pageable){
        return accountRepository.findAll(pageable)
                .map(account -> new AccountDto(account.getId(), account.getNumber(), account.getBalance()));
    }
}
