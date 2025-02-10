package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

public class CoursCryptoAnalyseRequestDTO {
    private String typeAnalyse; // "1er quartile", "max", "min", "moyenne", "ecart-type"
    private String crypto; // "Tous" ou nom spécifique de crypto
    private LocalDateTime dateMin;
    private LocalDateTime dateMax;

    // Constructeurs
    public CoursCryptoAnalyseRequestDTO() {}

    public CoursCryptoAnalyseRequestDTO(String typeAnalyse, String crypto, 
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
        // Types d'analyse valides
        List<String> typesAnalyseValides = List.of(
            "1er quartile", "max", "min", "moyenne", "ecart-type"
        );
        
        boolean typeAnalyseValide = typesAnalyseValides.contains(typeAnalyse);
        
        // Vérifier la crypto
        boolean cryptoValide = "Tous".equals(crypto) || 
            getCryptosDisponibles().contains(crypto);
        
        // Vérifier que les dates sont cohérentes
        boolean datesValides = dateMin != null && dateMax != null && 
            !dateMin.isAfter(dateMax);

        return typeAnalyseValide && cryptoValide && datesValides;
    }

    // Liste des cryptos disponibles
    public static List<String> getCryptosDisponibles() {
        return List.of(
            "Bitcoin", "Ethereum", "Litecoin", "Cardano", "Solana", 
            "Ripple", "Polkadot", "Dogecoin", "Chainlink", "Stellar"
        );
    }
}
