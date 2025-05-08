package com.marcelohofart.bank_api.requests;

import com.marcelohofart.bank_api.enums.TransactionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public class TransferRequest {
    @NotNull(message = "Tipo da transação é obrigatório")
    public UUID fromAccountId;
    @NotNull(message = "Tipo da transação é obrigatório")
    public UUID toAccountId;
    @NotNull(message = "A quantidade é obrigatória")
    @Positive(message = "A quantidade precisa de maior que zero")
    public BigDecimal amount;

    @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
    public String description;
}
