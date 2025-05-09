package com.marcelohofart.bank_api.controllers;

import com.marcelohofart.bank_api.dtos.AccountDto;
import com.marcelohofart.bank_api.requests.TransactionRequest;
import com.marcelohofart.bank_api.requests.TransferRequest;
import com.marcelohofart.bank_api.services.AccountService;
import com.marcelohofart.bank_api.services.TransactionService;
import com.marcelohofart.bank_api.services.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @Mock
    private TransactionService transactionService;

    @Mock
    private TransferService transferService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTransactionsInAccount() {
        // Cenário: Criando transações em uma conta com sucesso
        UUID accountId = UUID.randomUUID();
        List<TransactionRequest> transactions = List.of(new TransactionRequest());

        doNothing().when(transactionService).processTransactions(accountId, transactions);

        ResponseEntity<String> response = accountController.createTransactionsInAccount(accountId, transactions);

        assertEquals(HttpStatus.CREATED, response.getStatusCode()); // Verifica que a resposta tem o status CREATED
        assertEquals("Transações realizadas com sucesso!", response.getBody()); // Verifica a mensagem da resposta
        verify(transactionService).processTransactions(accountId, transactions); // Verifica que o serviço de transações foi chamado
    }

    @Test
    public void testTransferBetweenAccounts() {
        // Cenário: Realizando uma transferência entre contas com sucesso
        TransferRequest transferRequest = new TransferRequest();

        doNothing().when(transferService).processTransferBetweenAccounts(transferRequest);

        ResponseEntity<String> response = accountController.transferBetweenAccounts(transferRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode()); // Verifica que a resposta tem o status CREATED
        assertEquals("Transferência realizada com sucesso!", response.getBody()); // Verifica a mensagem da resposta
        verify(transferService).processTransferBetweenAccounts(transferRequest); // Verifica que o serviço de transferência foi chamado
    }

    @Test
    public void testGetAllAccounts_ValidPagination() {
        // Cenário: Obter todas as contas com paginação válida
        int page = 0;
        int size = 10;

        List<AccountDto> accountList = List.of(new AccountDto());
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "number"));
        Page<AccountDto> accountPage = new PageImpl<>(accountList, pageable, accountList.size());

        when(accountService.getAllAccounts(pageable)).thenReturn(accountPage);

        ResponseEntity<Page<AccountDto>> response = accountController.getAllAccounts(page, size);

        assertEquals(HttpStatus.OK, response.getStatusCode()); // Verifica que a resposta tem o status OK
        assertEquals(accountPage, response.getBody()); // Verifica o conteúdo da resposta
    }

    @Test
    public void testGetAccountDetails_Found() {
        // Cenário: Obter detalhes de conta com sucesso (conta encontrada)
        UUID accountId = UUID.randomUUID();
        Optional<AccountDto> accountDto = Optional.of(new AccountDto());

        when(accountService.getAccountDetailsById(accountId)).thenReturn(accountDto);

        ResponseEntity<Optional<AccountDto>> response = accountController.getAccountDetails(accountId);

        assertEquals(HttpStatus.OK, response.getStatusCode()); // Verifica que a resposta tem o status OK
        assertTrue(response.getBody().isPresent()); // Verifica que o corpo da resposta contém o DTO da conta
    }

    @Test
    public void testGetAccountDetails_NotFound() {
        // Cenário: Obter detalhes de conta (conta não encontrada)
        UUID accountId = UUID.randomUUID();

        when(accountService.getAccountDetailsById(accountId)).thenReturn(Optional.empty());

        ResponseEntity<Optional<AccountDto>> response = accountController.getAccountDetails(accountId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()); // Verifica que a resposta tem o status BAD_REQUEST
        assertTrue(response.getBody().isEmpty()); // Verifica que o corpo da resposta está vazio
    }

    @Test
    public void testGetAllAccounts_InvalidPage() {
        // Cenário: Solicitação com número de página inválido (menor que 0)
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountController.getAllAccounts(-1, 10);
        });
        assertEquals("O número da página não pode ser menor que zero.", exception.getMessage());
    }

    @Test
    public void testGetAllAccounts_InvalidPageSize() {
        // Cenário: Solicitação com tamanho de página inválido (maior que 50)
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountController.getAllAccounts(0, 100);
        });
        assertEquals("O tamanho da página não pode ser maior que 50.", exception.getMessage());
    }

}
