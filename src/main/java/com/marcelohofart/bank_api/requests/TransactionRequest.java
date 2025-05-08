package com.marcelohofart.bank_api.requests;

import com.marcelohofart.bank_api.enums.TransactionType;
import com.marcelohofart.bank_api.exceptions.InvalidTransactionRequestException;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public class TransactionRequest {
    public String transactionType;
    public BigDecimal amount;
    public String description;
    public void validate(){
        if (transactionType == null || transactionType.isBlank()) {
            throw new InvalidTransactionRequestException("O tipo da transação é obrigatório.");
        } else if (TransactionType.fromString(transactionType) == null) {
            throw new InvalidTransactionRequestException("Tipo de transação inválido. Valores aceitos: DEBIT, CREDIT.");
        }

        if (amount == null) {
            throw new InvalidTransactionRequestException("O valor é obrigatório.");
        } else if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionRequestException("O valor deve ser maior que zero.");
        }

        if (description != null && description.length() > 255) {
            throw new InvalidTransactionRequestException("A descrição deve ter no máximo 255 caracteres.");
        }
    }

}
