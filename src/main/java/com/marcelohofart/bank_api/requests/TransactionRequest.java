package com.marcelohofart.bank_api.requests;

import com.marcelohofart.bank_api.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class TransactionRequest {
    @NotNull(message = "Tipo da transação é obrigatório")
    public TransactionType transactionType;
    @NotNull(message = "A quantidade é obrigatória")
    @Positive(message = "A quantidade precisa de maior que zero")
    public BigDecimal amount;

    @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
    public String description;
}
