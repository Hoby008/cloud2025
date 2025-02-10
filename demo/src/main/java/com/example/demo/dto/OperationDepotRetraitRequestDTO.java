package com.example.demo.dto;

import java.math.BigDecimal;

public class OperationDepotRetraitRequestDTO {
    private String typeOperation; // "Depot" ou "Retrait"
    private BigDecimal montant;

    // Constructeurs
    public OperationDepotRetraitRequestDTO() {}

    public OperationDepotRetraitRequestDTO(String typeOperation, BigDecimal montant) {
        this.typeOperation = typeOperation;
        this.montant = montant;
    }

    // Getters et Setters
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
        // Vérifier le type d'opération
        boolean operationValide = "Depot".equals(typeOperation) || 
            "Retrait".equals(typeOperation);
        
        // Vérifier le montant
        boolean montantValide = montant != null && montant.compareTo(BigDecimal.ZERO) > 0;

        return operationValide && montantValide;
    }
}
