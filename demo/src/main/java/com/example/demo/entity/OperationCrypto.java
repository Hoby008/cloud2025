package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "operation_crypto")
public class OperationCrypto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "libelle", length = 50, nullable = false)
    private String libelle;

    // Constructeurs
    public OperationCrypto() {}

    public OperationCrypto(String libelle) {
        this.libelle = libelle;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
