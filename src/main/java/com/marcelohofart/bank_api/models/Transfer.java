package com.marcelohofart.bank_api.models;

import com.marcelohofart.bank_api.enums.TransactionType;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Transfers")
public class Transfer implements Serializable {
    public Transfer(BigDecimal amount, String description, Account originAccount, Account destinationAccount) {
        this.amount = amount;
        this.description = description;
        this.originAccount = originAccount;
        this.destinationAccount = destinationAccount;
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

    @Column(nullable = false)
    private LocalDateTime date = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "origin_account_id", nullable = false)
    private Account originAccount;

    @ManyToOne
    @JoinColumn(name = "destination_account_id", nullable = false)
    private Account destinationAccount;

}
