package com.marcelohofart.bank_api.models;

import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "Clients")
public class Client implements Serializable {
    protected Client() {
    }
    public Client(String name, String cpf, LocalDate birthDate) {
        this.name = name;
        this.cpf = cpf;
        this.birthDate = birthDate;
    }
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String cpf;

    private LocalDate birthDate;

    @OneToMany(mappedBy = "client")
    private List<Account> accounts;
}