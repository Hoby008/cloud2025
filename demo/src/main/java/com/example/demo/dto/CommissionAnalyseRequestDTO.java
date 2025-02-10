package com.example.demo.dto;

import java.time.LocalDateTime;

public class CommissionAnalyseRequestDTO {
    private String typeAnalyse; // "Somme" ou "Moyenne"
    private String crypto; // "Tous" ou nom spécifique de crypto
    private LocalDateTime dateMin;
    private LocalDateTime dateMax;

    // Constructeurs
    public CommissionAnalyseRequestDTO() {}

    public CommissionAnalyseRequestDTO(String typeAnalyse, String crypto, 
                                       LocalDateTime dateMin, LocalDateTime dateMax) {
        this.typeAnalyse = typeAnalyse;
        this.crypto = crypto;
        this.dateMin = dateMin;
        this.dateMax = dateMax;
    }

    // Getters et Setters
    public String getTypeAnalyse() {
        return typeAnalyse;
    }

    public void setTypeAnalyse(String typeAnalyse) {
        this.typeAnalyse = typeAnalyse;
    }

    public String getCrypto() {
        return crypto;
    }

    public void setCrypto(String crypto) {
        this.crypto = crypto;
    }

    public LocalDateTime getDateMin() {
        return dateMin;
    }

    public void setDateMin(LocalDateTime dateMin) {
        this.dateMin = dateMin;
    }

    public LocalDateTime getDateMax() {
        return dateMax;
    }

    public void setDateMax(LocalDateTime dateMax) {
        this.dateMax = dateMax;
    }

    // Méthode de validation
    public boolean isValid() {
        // Vérifier que les types d'analyse et de crypto sont valides
        boolean typeAnalyseValide = "Somme".equals(typeAnalyse) || "Moyenne".equals(typeAnalyse);
        boolean cryptoValide = "Tous".equals(crypto) || 
            getCryptosDisponibles().contains(crypto);
        
        // Vérifier que les dates sont cohérentes
        boolean datesValides = dateMin != null && dateMax != null && 
            !dateMin.isAfter(dateMax);

        return typeAnalyseValide && cryptoValide && datesValides;
    }

    // Liste des cryptos disponibles
    public static java.util.List<String> getCryptosDisponibles() {
        return java.util.Arrays.asList(
            "Bitcoin", "Ethereum", "Litecoin", "Cardano", "Solana", 
            "Ripple", "Polkadot", "Dogecoin", "Chainlink", "Stellar"
        );
    }
}
