package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OperationFiltreRequestDTO {
    // Filtres pour tous les types d'opérations
    private List<String> typesOperation; // Depot, Retrait, Achat, Vente
    private List<String> typesCrypto; // Bitcoin, Ethereum, etc.
    private LocalDateTime dateMin;
    private LocalDateTime dateMax;
    private BigDecimal montantMin;
    private BigDecimal montantMax;
    private List<String> etatsValidation; // En attente, Validé, Rejeté
    private String nomUtilisateur;

    // Pagination
    private int page = 0;
    private int taillePage = 20;

    // Constructeurs
    public OperationFiltreRequestDTO() {}

    // Getters et Setters
    public List<String> getTypesOperation() {
        return typesOperation;
    }

    public void setTypesOperation(List<String> typesOperation) {
        this.typesOperation = typesOperation;
    }

    public List<String> getTypesCrypto() {
        return typesCrypto;
    }

    public void setTypesCrypto(List<String> typesCrypto) {
        this.typesCrypto = typesCrypto;
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

    public BigDecimal getMontantMin() {
        return montantMin;
    }

    public void setMontantMin(BigDecimal montantMin) {
        this.montantMin = montantMin;
    }

    public BigDecimal getMontantMax() {
        return montantMax;
    }

    public void setMontantMax(BigDecimal montantMax) {
        this.montantMax = montantMax;
    }

    public List<String> getEtatsValidation() {
        return etatsValidation;
    }

    public void setEtatsValidation(List<String> etatsValidation) {
        this.etatsValidation = etatsValidation;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTaillePage() {
        return taillePage;
    }

    public void setTaillePage(int taillePage) {
        this.taillePage = taillePage;
    }

    // Méthodes de validation et de récupération des valeurs possibles
    public static List<String> getTypesOperationPossibles() {
        return List.of("Depot", "Retrait", "Achat", "Vente");
    }

    public static List<String> getTypesCryptoPossibles() {
        return List.of(
            "Bitcoin", "Ethereum", "Litecoin", "Cardano", "Solana", 
            "Ripple", "Polkadot", "Dogecoin", "Chainlink", "Stellar"
        );
    }

    public static List<String> getEtatsValidationPossibles() {
        return List.of("En attente", "Validé", "Rejeté");
    }
}
