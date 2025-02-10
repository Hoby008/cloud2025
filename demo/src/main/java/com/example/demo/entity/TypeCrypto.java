package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "type_crypto")
public class TypeCrypto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", length = 50, nullable = false, unique = true)
    private String nom;

    @Column(name = "symbole", length = 10, nullable = false)
    private String symbole;

    // Constructeurs
    public TypeCrypto() {}

    public TypeCrypto(String nom, String symbole) {
        this.nom = nom;
        this.symbole = symbole;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getSymbole() {
        return symbole;
    }

    public void setSymbole(String symbole) {
        this.symbole = symbole;
    }
}
