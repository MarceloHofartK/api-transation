package com.marcelohofart.bank_api.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Accounts")
public class Account implements Serializable {
    protected Account() {
    }
    public Account(String number, BigDecimal initialBalance, Client client, Agency agency) {
        this.number = number;
        this.balance = initialBalance;
        this.client = client;
        this.agency = agency;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true)
    private String number;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "agency_id", nullable = false)
    private Agency agency;

    @OneToMany(mappedBy = "originAccount")
    private List<Transfer> sentTransfers;

    @OneToMany(mappedBy = "destinationAccount")
    private List<Transfer> receivedTransfers;

    public BigDecimal getBalance(){
        return this.balance;
    }
    public UUID getId(){
        return this.id;
    }
    public String getNumber(){
        return this.number;
    }
}