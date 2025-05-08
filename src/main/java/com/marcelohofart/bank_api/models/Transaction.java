package com.marcelohofart.bank_api.models;

import com.marcelohofart.bank_api.enums.TransactionType;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "Transactions")
public class Transaction implements Serializable {
    protected Transaction() {
    }
    public Transaction(TransactionType transactionType, BigDecimal amount, String description, Account account, BigDecimal balanceBefore, BigDecimal balanceAfter) {
        this.type = transactionType;
        this.amount = amount;
        this.description = description;
        this.account = account;
        this.balanceAfter = balanceAfter;
        this.balanceBefore = balanceBefore;
        this.date = LocalDateTime.now();
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private BigDecimal amount;
    @Column(nullable = true)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type; // CREDIT or DEBIT

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false)
    private BigDecimal balanceBefore;

    @Column(nullable = false)
    private BigDecimal balanceAfter;

    @Column(nullable = false)
    private LocalDateTime date = LocalDateTime.now();

}
