package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OperationCryptoResponseDTO {
    private String typeCrypto;
    private String typeOperation;
    private BigDecimal montant;
    private BigDecimal commission;
    private BigDecimal prix;
    private BigDecimal nombreCrypto;
    private LocalDateTime dateHeure;

    // Constructeurs
    public OperationCryptoResponseDTO() {}

    public OperationCryptoResponseDTO(String typeCrypto, String typeOperation, 
                                      BigDecimal montant, BigDecimal commission, 
                                      BigDecimal prix, BigDecimal nombreCrypto, 
                                      LocalDateTime dateHeure) {
        this.typeCrypto = typeCrypto;
        this.typeOperation = typeOperation;
        this.montant = montant;
        this.commission = commission;
        this.prix = prix;
        this.nombreCrypto = nombreCrypto;
        this.dateHeure = dateHeure;
    }

    // Getters et Setters
    public String getTypeCrypto() {
        return typeCrypto;
    }

    public void setTypeCrypto(String typeCrypto) {
        this.typeCrypto = typeCrypto;
    }

    public String getTypeOperation() {
        return typeOperation;
    }

    public void setTypeOperation(String typeOperation) {
        this.typeOperation = typeOperation;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public BigDecimal getNombreCrypto() {
        return nombreCrypto;
    }

    public void setNombreCrypto(BigDecimal nombreCrypto) {
        this.nombreCrypto = nombreCrypto;
    }

    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(LocalDateTime dateHeure) {
        this.dateHeure = dateHeure;
    }
}
