package com.marcelohofart.bank_api.controllers;

import com.marcelohofart.bank_api.dtos.AccountDto;
import com.marcelohofart.bank_api.models.Account;
import com.marcelohofart.bank_api.models.Agency;
import com.marcelohofart.bank_api.models.Bank;
import com.marcelohofart.bank_api.models.Client;
import com.marcelohofart.bank_api.requests.TransactionRequest;
import com.marcelohofart.bank_api.requests.TransferRequest;
import com.marcelohofart.bank_api.services.AccountService;
import com.marcelohofart.bank_api.services.TransactionService;
import com.marcelohofart.bank_api.services.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@Tag(name = "Accounts", description = "Operações relacionadas a contas bancárias")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransferService transferService;

    @Operation(
        summary = "Realiza um ou mais lançamentos em uma conta específica. tipos de transação aceitos -> DEBIT | CREDIT",
        description = "Permite realizar lançamentos em uma conta bancária, como saques e depósitos. Tipos de transação permitidos: DEBIT (saque) ou CREDIT (depósito). Exemplo de saque: débito de um valor da conta. Exemplo de depósito: crédito de um valor na conta."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Transações realizadas com sucesso!"),
        @ApiResponse(responseCode = "400", description = "Saldo insuficiente na conta para realizar a transação."),
        @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @PostMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<String> createTransactionsInAccount(
            @Parameter(description = "Identificador da conta que receberá os lançamentos", example = "f81d4fae-7dec-11d0-a765-00a0c91e6bf6")
            @PathVariable UUID accountId,
            @Parameter(description = "Lista de transações a serem realizadas")
            @RequestBody List<TransactionRequest> transactionRequestList
    ) {
        transactionService.processTransactions(accountId, transactionRequestList);
        return ResponseEntity.status(HttpStatus.CREATED).body("Transações realizadas com sucesso!");
    }

    @Operation(
            summary = "Realiza uma transferência entre contas.",
            description = "Permite transferir dinheiro de uma conta para outra de forma segura."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transferência realizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Saldo insuficiente na conta para realizar a transação."),
            @ApiResponse(responseCode = "404", description = "Conta de origem não encontrada ou conta de destino não encontrada")
    })
    @PostMapping("/accounts/transfer")
    public ResponseEntity<String> transferBetweenAccounts(
            @Parameter(description = "Transferência que será feita entre as contas")
            @RequestBody TransferRequest transferRequest
    ) {
        transferService.processTransferBetweenAccounts(transferRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Transferência realizada com sucesso!");
    }

    @Operation(
        summary = "Listar contas cadastradas",
        description = "Retorna uma lista paginada de contas bancárias cadastradas no sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista paginada de contas retornada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos")
    })
    @GetMapping("/accounts")
    public ResponseEntity<Page<AccountDto>> getAllAccounts(
            @Parameter(description = "Número da página (começa em 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Tamanho da página (máximo: 50)", example = "20")
            @RequestParam(defaultValue = "20") int page_size
    ) {
        if (page < 0) {
            throw new IllegalArgumentException("O número da página não pode ser menor que zero.");
        }

        if (page_size > 50) {
            throw new IllegalArgumentException("O tamanho da página não pode ser maior que 50.");
        }

        Pageable pageable = PageRequest.of(page, page_size, Sort.by(Sort.Direction.ASC, "number"));

        Page<AccountDto> accounts = accountService.getAllAccounts(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(accounts);
    }

    @Operation(
            summary = "Buscar os detalhes de uma conta",
            description = "Retorna um objeto com detalhes de uma conta."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalhes retornados com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<Optional<AccountDto>> getAccountDetails(
            @Parameter(description = "Identificador da conta", example = "f81d4fae-7dec-11d0-a765-00a0c91e6bf6")
            @Valid @PathVariable UUID accountId
    ) {
        Optional<AccountDto> accountDto = accountService.getAccountDetailsById(accountId);
        if(accountDto.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(accountDto);
    }
}
