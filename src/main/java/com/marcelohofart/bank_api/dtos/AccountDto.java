package com.marcelohofart.bank_api.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountDto {
    @NotNull(message = "o identificador é obrigatório")
    public UUID id;
    @NotNull(message = "O número da conta é obrigatório")
    @PositiveOrZero(message = "O número da conta precisa ser positivo")
    public String number;
    @NotNull(message = "A quantidade é obrigatória")
    @PositiveOrZero(message = "A quantidade de dinheiro na conta não pode ser negativa")
    public BigDecimal balance;
    public AccountDto() {}
    public AccountDto(UUID id, String number, BigDecimal balance){
        this.id = id;
        this.number = number;
        this.balance = balance;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
