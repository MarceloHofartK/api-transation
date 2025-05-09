package com.marcelohofart.bank_api.configs;

import com.marcelohofart.bank_api.repositories.AccountRepository;
import com.marcelohofart.bank_api.repositories.AgencyRepository;
import com.marcelohofart.bank_api.repositories.BankRepository;
import com.marcelohofart.bank_api.repositories.ClientRepository;
import com.marcelohofart.bank_api.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Configuration
@Profile("dev")
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private AgencyRepository agencyRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void run(String... args) throws Exception {
        Bank bank = bankRepository.save(new Bank("Matera Bank"));

        Agency agency1 = agencyRepository.save(new Agency("Agência 01", "01", bank));
        Agency agency2 = agencyRepository.save(new Agency("Agência 02", "02", bank));

        Client marcelo = clientRepository.save(new Client("Marcelo", "00100200325", LocalDate.of(2004, 1, 26)));
        Client client2 = clientRepository.save(new Client("Cliente02", "00100200335", LocalDate.of(2002, 5, 16)));
        Client client3 = clientRepository.save(new Client("Cliente03", "98765432100", LocalDate.of(2000, 10, 8)));

        accountRepository.save(new Account("000001", new BigDecimal("1500.00"), marcelo, agency1));
        accountRepository.save(new Account("000002", new BigDecimal("2500.00"), client2, agency1));
        accountRepository.save(new Account("000003", new BigDecimal("300.00"), client3, agency2));
        accountRepository.save(new Account("000004", new BigDecimal("750.50"), marcelo, agency2));
    }
}