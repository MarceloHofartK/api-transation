package com.marcelohofart.bank_api.enums;

public enum TransactionType {
    CREDIT,
    DEBIT;

    public static TransactionType fromString(String value) {
        try {
            return TransactionType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }
}