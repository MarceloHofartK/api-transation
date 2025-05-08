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

    public AccountDto(UUID id, String number, BigDecimal balance){
        this.id = id;
        this.number = number;
        this.balance = balance;
    }
}
