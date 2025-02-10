package com.example.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "commission")
public class Commission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pourcentage", precision = 5, scale = 2, nullable = false)
    private BigDecimal pourcentage;

    @ManyToOne
    @JoinColumn(name = "operation_crypto_id", nullable = false)
    private OperationCrypto operationCrypto;

    @ManyToOne
    @JoinColumn(name = "type_crypto_id", nullable = false)
    private TypeCrypto typeCrypto;

    @Column(name = "date_heure_commission", nullable = false)
    private LocalDateTime dateHeureCommission;

    // Constructeurs
    public Commission() {}

    public Commission(BigDecimal pourcentage, OperationCrypto operationCrypto, 
                      TypeCrypto typeCrypto, LocalDateTime dateHeureCommission) {
        this.pourcentage = pourcentage;
        this.operationCrypto = operationCrypto;
        this.typeCrypto = typeCrypto;
        this.dateHeureCommission = dateHeureCommission;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(BigDecimal pourcentage) {
        this.pourcentage = pourcentage;
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

    public LocalDateTime getDateHeureCommission() {
        return dateHeureCommission;
    }

    public void setDateHeureCommission(LocalDateTime dateHeureCommission) {
        this.dateHeureCommission = dateHeureCommission;
    }
}
