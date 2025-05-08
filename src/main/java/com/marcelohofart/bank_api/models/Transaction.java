package com.marcelohofart.bank_api.models;

import com.marcelohofart.bank_api.enums.TransactionType;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Transactions")
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

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
