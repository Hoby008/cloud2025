package com.example.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cours_cryptomonnaie")
public class CoursCryptomonnaie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "type_crypto_id", nullable = false)
    private TypeCrypto typeCrypto;

    @Column(name = "prix", precision = 18, scale = 8, nullable = false)
    private BigDecimal prix;

    @Column(name = "date_heure", nullable = false)
    private LocalDateTime dateHeure;

    // Constructeurs
    public CoursCryptomonnaie() {}

    public CoursCryptomonnaie(TypeCrypto typeCrypto, BigDecimal prix, LocalDateTime dateHeure) {
        this.typeCrypto = typeCrypto;
        this.prix = prix;
        this.dateHeure = dateHeure;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeCrypto getTypeCrypto() {
        return typeCrypto;
    }

    public void setTypeCrypto(TypeCrypto typeCrypto) {
        this.typeCrypto = typeCrypto;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(LocalDateTime dateHeure) {
        this.dateHeure = dateHeure;
    }
}
