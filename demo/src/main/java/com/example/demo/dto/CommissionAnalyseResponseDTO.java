package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class CommissionAnalyseResponseDTO {
    private String typeAnalyse;
    private String crypto;
    private BigDecimal resultat;
    private List<Map<String, Object>> details;

    // Constructeurs
    public CommissionAnalyseResponseDTO() {}

    public CommissionAnalyseResponseDTO(String typeAnalyse, String crypto, 
                                        BigDecimal resultat, 
                                        List<Map<String, Object>> details) {
        this.typeAnalyse = typeAnalyse;
        this.crypto = crypto;
        this.resultat = resultat;
        this.details = details;
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

    public BigDecimal getResultat() {
        return resultat;
    }

    public void setResultat(BigDecimal resultat) {
        this.resultat = resultat;
    }

    public List<Map<String, Object>> getDetails() {
        return details;
    }

    public void setDetails(List<Map<String, Object>> details) {
        this.details = details;
    }
}
