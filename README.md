# Projeto - Desafio de Lançamento MATERA

Este projeto utiliza java 17 com Spring Boot 3 como back-end e PostgreSQL como banco de dados, com configuração automatizada via Docker Compose.

## 🔧 Tecnologias Utilizadas

- Java 17 (Spring Boot 3)
- PostgreSQL
- Docker & Docker Compose
- JPA / Hibernate

## 🚀 Executando o projeto

### 1. Pré-requisitos

- Docker
- Docker Compose
- Java 17
- Maven

### 2. Estrutura do projeto

preciso escrever aqui quando terminar

### 3. Configuração do Docker Compose

O arquivo `docker-compose.yml` inicializa a api e o banco de dados PostgreSQL criando o database bank_api (localizado em `docker/init.sql`):

- Usuário: `postgres`
- Senha: `1234`
- Banco: `bank_api`

### 4. Subindo o ambiente

Para subir todo o ambiente e a API basta rodar o comando `docker-compose up` na raiz do projeto.


### 5. Adicionar novas funcionalidades

Para adicionar mais funcionalidades na imagem do docker é preciso rodar o comando `mvn clean install` 
na raiz do projeto(irá gerar um arquivo jar na pasta 'target'). Esse arquivo jar já está configurado corretamente no Dockerfile então
basta rodar o comando `docker-compose up --build` para atualizar as imagens no docker.


