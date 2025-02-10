package com.example.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "fond")
public class Fond {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "portefeuille_id", nullable = false, unique = true)
    private Portefeuille portefeuille;

    @Column(name = "montant_actuel", precision = 19, scale = 2, nullable = false)
    private BigDecimal montantActuel;

    // Constructeurs
    public Fond() {
        this.montantActuel = BigDecimal.ZERO;
    }

    public Fond(Portefeuille portefeuille) {
        this.portefeuille = portefeuille;
        this.montantActuel = BigDecimal.ZERO;
    }

    // Méthode de dépôt
    public void deposer(BigDecimal montant) {
        if (montant.compareTo(BigDecimal.ZERO) > 0) {
            this.montantActuel = this.montantActuel.add(montant);
        } else {
            throw new IllegalArgumentException("Le montant du dépôt doit être positif");
        }
    }

    // Méthode de retrait
    public void retirer(BigDecimal montant) {
        if (montant.compareTo(BigDecimal.ZERO) > 0) {
            if (this.montantActuel.compareTo(montant) >= 0) {
                this.montantActuel = this.montantActuel.subtract(montant);
            } else {
                throw new IllegalArgumentException("Solde insuffisant");
            }
        } else {
            throw new IllegalArgumentException("Le montant du retrait doit être positif");
        }
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Portefeuille getPortefeuille() {
        return portefeuille;
    }

    public void setPortefeuille(Portefeuille portefeuille) {
        this.portefeuille = portefeuille;
    }

    public BigDecimal getMontantActuel() {
        return montantActuel;
    }

    public void setMontantActuel(BigDecimal montantActuel) {
        this.montantActuel = montantActuel;
    }
}
