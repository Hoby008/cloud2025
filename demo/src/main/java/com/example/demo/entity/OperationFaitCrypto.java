package com.example.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "operation_fait_crypto")
public class OperationFaitCrypto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_heure", nullable = false)
    private LocalDateTime dateHeure;

    @ManyToOne
    @JoinColumn(name = "operation_crypto_id", nullable = false)
    private OperationCrypto operationCrypto;

    @ManyToOne
    @JoinColumn(name = "type_crypto_id", nullable = false)
    private TypeCrypto typeCrypto;

    @Column(name = "commission", precision = 5, scale = 2, nullable = false)
    private BigDecimal commission;

    @Column(name = "nombre", precision = 18, scale = 8, nullable = false)
    private BigDecimal nombre;

    @Column(name = "prix_final", precision = 18, scale = 8, nullable = false)
    private BigDecimal prixFinal;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private ValidationUser utilisateur;

    // Constructeurs
    public OperationFaitCrypto() {}

    public OperationFaitCrypto(OperationCrypto operationCrypto, TypeCrypto typeCrypto, 
                                BigDecimal commission, BigDecimal nombre, 
                                BigDecimal prixFinal, ValidationUser utilisateur) {
        this.dateHeure = LocalDateTime.now();
        this.operationCrypto = operationCrypto;
        this.typeCrypto = typeCrypto;
        this.commission = commission;
        this.nombre = nombre;
        this.prixFinal = prixFinal;
        this.utilisateur = utilisateur;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(LocalDateTime dateHeure) {
        this.dateHeure = dateHeure;
    }

    public OperationCrypto getOperationCrypto() {
        return operationCrypto;
    }

    public void setOperationCrypto(OperationCrypto operationCrypto) {
        this.operationCrypto = operationCrypto;
    }

    public TypeCrypto getTypeCrypto() {
        return typeCrypto;
    }

    public void setTypeCrypto(TypeCrypto typeCrypto) {
        this.typeCrypto = typeCrypto;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public BigDecimal getNombre() {
        return nombre;
    }

    public void setNombre(BigDecimal nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrixFinal() {
        return prixFinal;
    }

    public void setPrixFinal(BigDecimal prixFinal) {
        this.prixFinal = prixFinal;
    }

    public ValidationUser getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(ValidationUser utilisateur) {
        this.utilisateur = utilisateur;
    }
}
