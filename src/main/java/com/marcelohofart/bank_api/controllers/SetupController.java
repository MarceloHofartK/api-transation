package com.marcelohofart.bank_api.controllers;

import com.marcelohofart.bank_api.models.Account;
import com.marcelohofart.bank_api.models.Agency;
import com.marcelohofart.bank_api.models.Bank;
import com.marcelohofart.bank_api.models.Client;
import com.marcelohofart.bank_api.repositories.AccountRepository;
import com.marcelohofart.bank_api.repositories.AgencyRepository;
import com.marcelohofart.bank_api.repositories.BankRepository;
import com.marcelohofart.bank_api.repositories.ClientRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@Profile("dev")
@Tag(name = "Setup", description = "Adiciona os dados necessários no banco de dados (Apenas para perfil de Desenvolvedor)")
public class SetupController {

    // injetei os repositórios direto no controller
    // apenas para fins de teste, para permitir que quem estiver avaliando a aplicação consiga
    // acessar os dados facilmente.
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AgencyRepository agencyRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Operation(
            summary = "Popular banco de dados com dados iniciais para testes",
            description = """
                Cria registros de exemplo para bancos, agências, clientes e contas. \
                Este endpoint deve ser usado apenas em ambiente de desenvolvimento \
                (requer o perfil 'dev' ativo: `spring.profiles.active=dev`).
                """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dados adicionados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Os dados já foram criados anteriormente")
    })
    @PostMapping("/setup-application")
    public ResponseEntity<String> setupAplicationDebug(){
        if (bankRepository.count() == 0) {
            // apenas irá aparecer no Swagger se tiver com o perfil 'dev' (spring.profiles.active=dev)
            // no application.properties
            Bank materaBank = bankRepository.save(new Bank("Matera Bank"));
            Agency agency1 = agencyRepository.save(new Agency("Agência 01", "01", materaBank));
            Agency agency2 = agencyRepository.save(new Agency("Agência 02", "02", materaBank));
            Client marcelo = clientRepository.save(new Client("Marcelo", "00100200325", LocalDate.of(2004, 1, 26)));
            Client client2 = clientRepository.save(new Client("Cliente02", "00100200335", LocalDate.of(2002, 5, 16)));
            Client client3 = clientRepository.save(new Client("Cliente03", "98765432100", LocalDate.of(2000, 10, 8)));
            Account account1 = accountRepository.save(new Account("000001", new BigDecimal("1500.00"), marcelo, agency1));
            Account account2 = accountRepository.save(new Account("000002", new BigDecimal("2500.00"), client2, agency1));
            Account account3 = accountRepository.save(new Account("000003", new BigDecimal("300.00"), client3, agency2));
            Account account4 = accountRepository.save(new Account("000004", new BigDecimal("750.50"), marcelo, agency2));

            return ResponseEntity.status(HttpStatus.CREATED).body("Dados adicionados com sucesso!");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Os dados já foram criados!");
    }
}
