package com.marcelohofart.bank_api.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Banks")
public class Bank implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "bank")
    private List<Agency> agencies;
}