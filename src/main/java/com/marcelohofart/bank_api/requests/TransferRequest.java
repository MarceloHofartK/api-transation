package com.marcelohofart.bank_api.requests;

import com.marcelohofart.bank_api.enums.TransactionType;
import com.marcelohofart.bank_api.exceptions.InvalidTransactionRequestException;
import com.marcelohofart.bank_api.exceptions.InvalidTransferRequestException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransferRequest {

    public UUID fromAccountId;
    public UUID toAccountId;
    public BigDecimal amount;
    public String description;

    public void validate() {
        if (fromAccountId == null) throw new InvalidTransferRequestException("ID da conta de origem é obrigatório.");

        if (toAccountId == null) throw new InvalidTransferRequestException("ID da conta de destino é obrigatório.");

        if (fromAccountId == toAccountId) throw new InvalidTransferRequestException("As contas não podem ser iguais.");

        if (amount == null) {
            throw new InvalidTransferRequestException("A quantidade é obrigatória.");
        } else if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransferRequestException("A quantidade precisa ser maior que zero.");
        }

        if (description != null && description.length() > 255) {
            throw new InvalidTransferRequestException("A descrição deve ter no máximo 255 caracteres.");
        }
    }
}