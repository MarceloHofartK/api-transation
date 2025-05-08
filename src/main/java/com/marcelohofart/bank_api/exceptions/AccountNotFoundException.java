package com.marcelohofart.bank_api.exceptions;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(){
        super("Conta não encontrada");
    }
    public AccountNotFoundException(String message){
        super(message);
    }
}
