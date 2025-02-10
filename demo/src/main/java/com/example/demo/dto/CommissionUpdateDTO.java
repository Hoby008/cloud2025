package com.example.demo.dto;

import java.math.BigDecimal;

public class CommissionUpdateDTO {
    private Long id;
    private String typeCrypto;
    private String operationType;
    private BigDecimal pourcentage;

    // Constructeurs
    public CommissionUpdateDTO() {}

    public CommissionUpdateDTO(Long id, String typeCrypto, String operationType, BigDecimal pourcentage) {
        this.id = id;
        this.typeCrypto = typeCrypto;
        this.operationType = operationType;
        this.pourcentage = pourcentage;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeCrypto() {
        return typeCrypto;
    }

    public void setTypeCrypto(String typeCrypto) {
        this.typeCrypto = typeCrypto;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public BigDecimal getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(BigDecimal pourcentage) {
        this.pourcentage = pourcentage;
    }

    // Validation des données
    public boolean isValid() {
        return id != null && 
               typeCrypto != null && !typeCrypto.isEmpty() &&
               operationType != null && !operationType.isEmpty() &&
               pourcentage != null && 
               pourcentage.compareTo(BigDecimal.ZERO) > 0 && 
               pourcentage.compareTo(BigDecimal.valueOf(10)) < 0;
    }
}
