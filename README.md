# 🏦 API de Lançamentos Bancários

Esta API RESTful foi desenvolvida em Java 17 com Spring Boot 3 e tem como objetivo gerenciar **lançamentos bancários** de débito e crédito. Suporta múltiplos lançamentos por requisição, controle de concorrência pessimista para garantir a integridade dos dados.

## 🚀 Tecnologias Utilizadas

- Java 17
- Spring Boot 3
- Swagger 
- Spring Data JPA
- PostgreSQL (produção/desenvolvimento)
- Maven
- JUnit 5 + Mockito (testes)

## 📦 Funcionalidades

- ✅ Cadastro de lançamentos (débito e crédito)
- 🔁 Suporte a múltiplos lançamentos por requisição
- ✅ Transferência de dinheiro entre contas
- 🔒 Controle de concorrência pessimista
- 🧾 Consulta de saldo
- 🧪 Testes unitários

## 🏛️ Arquitetura da Aplicação

A arquitetura da aplicação segue o padrão **Layered Architecture** (Arquitetura em Camadas), onde cada camada tem uma responsabilidade bem definida, proporcionando uma melhor organização e manutenção do código. As camadas principais são:

#### 1. **Models (Entidade)**

A camada de **Models** contém as entidades que representam as tabelas do banco de dados. Cada entidade é anotada com as anotações do JPA, como `@Entity` e `@Id`, para definir a estrutura das tabelas e as relações entre elas.

- Exemplo: `Account`, `Transfer`, `Bank`, etc.

#### 2. **Repositories**

A camada de **Repositories** é responsável pela interação direta com o banco de dados. Os repositórios utilizam o **Spring Data JPA**, estendendo a interface `JpaRepository`, o que proporciona métodos prontos para operações CRUD (Create, Read, Update, Delete) e consultas customizadas.

- Exemplo: `AccountRepository`, `TransferRepository`, etc.

#### 3. **Services**

A camada de **Service** contém a lógica de negócio da aplicação. É nela que você implementa as regras e processos, como validações, cálculos, e manipulação dos dados entre o **Controller** e o **Repository**. A camada de serviço é uma abstração da lógica de negócios e serve como intermediária entre o **Controller** e o **Repository**.

- Exemplo: `AccountService`, `TransferService`, etc.

#### 4. **Controllers**

A camada de **Controller** é responsável por expor os endpoints da API REST. Ela recebe as requisições HTTP, delega a lógica para a camada de **Service**, e retorna a resposta apropriada ao cliente. O **Controller** é responsável apenas por gerenciar as requisições e não contém lógica de negócio.

- Exemplo: `AccountController`

#### 5. **DTOs, Requests e Responses**

Os **DTOs, Requests e Responses** são utilizados para transferir dados entre as camadas da aplicação de forma mais eficiente e segura. Eles evitam o acoplamento direto entre a camada de **Controller** e **Model**, permitindo a conversão de dados de forma controlada. Geralmente, você terá DTOs para requisições (Request) e respostas (Response).

- Exemplo: `AccountDto`, `TransferResponse`, etc.

#### 6. **ControllerAdvice (Tratamento de Erros)**

O **ControllerAdvice** é usado para tratar exceções globalmente e gerar respostas consistentes para erros. Ele permite capturar exceções específicas (como `InsufficientBalanceException`, `AccountNotFoundException`, etc.) e retornar um erro HTTP com a mensagem apropriada.

- Exemplo: `InsufficientBalanceExceptionHandler` que trata o erro de saldo insuficiente e lança respostas personalizadas.

### Fluxo de Execução

Quando uma requisição chega na API, ela passa pelo **Controller**, que delega a execução da lógica para o **Service**. O **Service** interage com o **Repository** para buscar ou manipular os dados e, por fim, retorna uma **response** que será enviada como resposta ao cliente. Se ocorrer algum erro, ele será tratado pelo **ControllerAdvice**, que garante uma resposta consistente para o usuário.

### Benefícios da Arquitetura

- **Separação de responsabilidades**: Cada camada tem uma responsabilidade clara, facilitando a manutenção e evolução do código.
- **Facilidade de testes**: Cada camada pode ser testada de forma isolada.
- **Escalabilidade**: A arquitetura facilita a adição de novas funcionalidades sem impactar demais as camadas existentes.


## 🚀 Como executar o projeto?

### 1. Pré-requisitos

- Java 17
- Maven 3.9.9
- Docker
- Docker Compose

### 2. Subindo o Banco de Dados com Docker

Antes de rodar a aplicação, é necessário iniciar o banco de dados PostgreSQL com Docker Compose. Execute o seguinte comando na raiz do projeto:
`docker-compose -f docker-compose.dev.yml up -d`

### 3. Iniciar a aplicação
Para iniciar a aplicação é preciso rodar o comando `mvn spring-boot:run` na raiz do projeto,
esse comando irá iniciar a aplicação com o perfil 'dev', com esse perfil ativo será gerado dados iniciais no banco de dados. 
- Exemplo: `Bank`, `Account`, `Agency`, etc.
* Essa configuração está na classe **.../com.marcelohofart.bank_api/configs/DatabaseSeeder.java** 

### 4. Abrir o swagger no navegador
Após iniciar a aplicação, será gerado uma URL do swagger-ui que irá direcionar para a documentação dos endpoints.
- URL: `http://localhost:8080/swagger-ui.html`

## 🧪 Como Realizar Testes Unitários
Para realizar todos os testes unitários da aplicação, na raiz do projeto utilize o comando Maven:
`mvn test`

## ✨ Como Criar Novas Funcionalidades

Para adicionar novas funcionalidades à API, siga os passos abaixo respeitando a estrutura e boas práticas do projeto:

1. **Model (Entidade)**  
   Crie a classe no pacote `models` com as anotações do JPA (`@Entity`, `@Id`, etc).

2. **Repository**  
   Crie uma interface no pacote `repositories` estendendo `JpaRepository`.

3. **DTOs**  
   Crie os DTOs nos pacotes `dtos, requests, responses` para representar os dados de entrada e saída.

4. **Service**  
   No pacote `services`, implemente a regra de negócio, fazendo as conversões entre DTOs e entidades.

5. **Controller**  
   Crie o endpoint REST no pacote `controller`, chamando os métodos da camada de serviço.

6. **Testes**  
   Adicione testes unitários.

> ✅ Mantenha o padrão de organização do projeto e evite regras de negócio diretamente nos controllers.

## ⚠️ Tratamento de Erros Personalizados

A aplicação utiliza o padrão `@ControllerAdvice` para capturar e tratar exceções de forma centralizada, garantindo respostas consistentes e personalizadas para os erros. O `RestExceptionHandler` intercepta as exceções lançadas pela aplicação e retorna uma mensagem de erro específica para cada tipo de exceção.

### Exceções Tratadas:

- **InsufficientBalanceException**  
  Retorna um erro `400 Bad Request` com a mensagem de saldo insuficiente.

- **AccountNotFoundException**  
  Retorna um erro `404 Not Found` quando a conta não for encontrada.

- **IllegalArgumentException**  
  Retorna um erro `400 Bad Request` quando os argumentos fornecidos são inválidos.

- **InvalidTransactionRequestException**  
  Retorna um erro `400 Bad Request` para requisições de transações inválidas.

- **InvalidTransferRequestException**  
  Retorna um erro `400 Bad Request` para requisições de transferências inválidas.

## 💻 Autor
Desenvolvido por Marcelo Hofart – [LinkedIn](https://www.linkedin.com/in/marcelo-hofart/) | [GitHub](https://github.com/MarceloHofartK)
