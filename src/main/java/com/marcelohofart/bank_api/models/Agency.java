package com.marcelohofart.bank_api.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "Agencies")
public class Agency implements Serializable {
    protected Agency() {
    }
    public Agency(String name, String number, Bank bank) {
        this.name = name;
        this.number = number;
        this.bank = bank;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private String number;
    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "bank_id", nullable = false)
    private Bank bank;

    @OneToMany(mappedBy = "agency")
    private List<Account> accounts;
}