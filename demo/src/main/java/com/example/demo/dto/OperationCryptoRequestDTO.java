package com.example.demo.dto;

import java.math.BigDecimal;

public class OperationCryptoRequestDTO {
    private String typeCrypto;
    private String typeOperation; // "Achat" ou "Vente"
    private BigDecimal montant; // Montant en euros pour l'achat, ou nombre de crypto pour la vente

    // Constructeurs
    public OperationCryptoRequestDTO() {}

    public OperationCryptoRequestDTO(String typeCrypto, String typeOperation, BigDecimal montant) {
        this.typeCrypto = typeCrypto;
        this.typeOperation = typeOperation;
        this.montant = montant;
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

    // Méthode de validation
    public boolean isValid() {
        // Vérifier le type de crypto
        boolean cryptoValide = CoursCryptoAnalyseRequestDTO.getCryptosDisponibles()
            .contains(typeCrypto);
        
        // Vérifier le type d'opération
        boolean operationValide = "Achat".equals(typeOperation) || 
            "Vente".equals(typeOperation);
        
        // Vérifier le montant
        boolean montantValide = montant != null && montant.compareTo(BigDecimal.ZERO) > 0;

        return cryptoValide && operationValide && montantValide;
    }
}
