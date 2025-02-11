package com.exemple.Cloud.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "portefeuilles")
public class Portefeuille {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Clé primaire

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true) // Relation avec User
    private User user;

    @Column(nullable = false)
    private BigDecimal solde;

    private LocalDateTime dateCreation;
    private LocalDateTime dateMaj;

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
        dateMaj = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dateMaj = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateMaj() {
        return dateMaj;
    }

    public void setDateMaj(LocalDateTime dateMaj) {
        this.dateMaj = dateMaj;
    }
}
