package com.marcelohofart.bank_api.requests;

import com.marcelohofart.bank_api.enums.TransactionType;
import com.marcelohofart.bank_api.exceptions.InvalidTransactionRequestException;
import com.marcelohofart.bank_api.exceptions.InvalidTransferRequestException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransferRequest {

    @Schema(description = "Conta que irá enviar o dinheiro", example = "f81d4fae-7dec-11d0-a765-00a0c91e6bf6", defaultValue = "f81d4fae-7dec-11d0-a765-00a0c91e6bf6")
    public UUID fromAccountId;
    @Schema(description = "Conta que irá receber o dinheiro", example = "f81d4fae-7dec-11d0-a765-0023c91e6h32", defaultValue = "f81d4fae-7dec-11d0-a765-0023c91e6h32")
    public UUID toAccountId;
    @Schema(description = "Quantidade de dinheiro que irá transferir", example = "10", defaultValue = "0")
    public BigDecimal amount;
    @Schema(description = "[Opcional] Descrição do motivo da transferência", example = "Pagamento de boleto", defaultValue = "Pagamento de boleto")
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