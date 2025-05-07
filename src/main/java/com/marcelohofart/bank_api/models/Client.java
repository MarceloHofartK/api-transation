package com.marcelohofart.bank_api.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "Clients")
public class Client implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @Column(unique = true)
    private String cpf;

    private LocalDate birthDate;

    @OneToMany(mappedBy = "client")
    private List<Account> accounts;
}