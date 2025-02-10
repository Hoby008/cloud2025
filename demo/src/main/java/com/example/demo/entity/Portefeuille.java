package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "portefeuille")
public class Portefeuille {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private ValidationUser utilisateur;

    // Constructeurs
    public Portefeuille() {}

    public Portefeuille(ValidationUser utilisateur) {
        this.utilisateur = utilisateur;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ValidationUser getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(ValidationUser utilisateur) {
        this.utilisateur = utilisateur;
    }
}
