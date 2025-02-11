package com.exemple.Cloud.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTransactions;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // ID utilisateur

    private String type; // achat, vente, depot, retrait

    private Long cryptomonnaieId; // ID cryptomonnaie

    private BigDecimal montant;

    private BigDecimal prix; // Prix au moment de la transaction

    private LocalDateTime dateTransaction;

    @PrePersist
    protected void onCreate() {
        dateTransaction = LocalDateTime.now();
    }

    // Constructeur par défaut
    public Transaction() {
    }

    // Constructeur avec paramètres
    public Transaction(User user, String type, Long cryptomonnaieId, BigDecimal montant, BigDecimal prix) {
        this.user = user;
        this.type = type;
        this.cryptomonnaieId = cryptomonnaieId;
        this.montant = montant;
        this.prix = prix;
        this.dateTransaction = LocalDateTime.now();
    }

    public Long getIdTransactions() {
        return idTransactions;
    }

    public void setIdTransactions(Long idTransactions) {
        this.idTransactions = idTransactions;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCryptomonnaieId() {
        return cryptomonnaieId;
    }

    public void setCryptomonnaieId(Long cryptomonnaieId) { 
        this.cryptomonnaieId = cryptomonnaieId;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public LocalDateTime getDateTransaction() {
        return dateTransaction;
    }

    public void setDateTransaction(LocalDateTime dateTransaction) {
        this.dateTransaction = dateTransaction;
    }
}
