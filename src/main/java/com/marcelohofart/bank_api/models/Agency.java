package com.marcelohofart.bank_api.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "Agencies")
public class Agency implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String number;
    private String name;

    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @OneToMany(mappedBy = "agency")
    private List<Account> accounts;
}