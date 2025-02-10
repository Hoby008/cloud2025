package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "operation_validation")
public class OperationValidation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "type_operation", nullable = false)
    private String typeOperation; // "Depot", "Retrait", "Achat", "Vente"

    @Column(name = "details_operation", columnDefinition = "TEXT")
    private String detailsOperation;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private ValidationUser utilisateur;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_expiration", nullable = false)
    private LocalDateTime dateExpiration;

    @Column(name = "est_valide", nullable = false)
    private Boolean estValide;

    @Column(name = "date_validation")
    private LocalDateTime dateValidation;

    // Constructeurs
    public OperationValidation() {}

    public OperationValidation(ValidationUser utilisateur, String typeOperation, String detailsOperation) {
        this.token = UUID.randomUUID().toString();
        this.typeOperation = typeOperation;
        this.detailsOperation = detailsOperation;
        this.utilisateur = utilisateur;
        this.dateCreation = LocalDateTime.now();
        this.dateExpiration = dateCreation.plusHours(1); // Expiration après 1 heure
        this.estValide = false;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTypeOperation() {
        return typeOperation;
    }

    public void setTypeOperation(String typeOperation) {
        this.typeOperation = typeOperation;
    }

    public String getDetailsOperation() {
        return detailsOperation;
    }

    public void setDetailsOperation(String detailsOperation) {
        this.detailsOperation = detailsOperation;
    }

    public ValidationUser getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(ValidationUser utilisateur) {
        this.utilisateur = utilisateur;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDateTime dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public Boolean getEstValide() {
        return estValide;
    }

    public void setEstValide(Boolean estValide) {
        this.estValide = estValide;
        if (estValide) {
            this.dateValidation = LocalDateTime.now();
        }
    }

    public LocalDateTime getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(LocalDateTime dateValidation) {
        this.dateValidation = dateValidation;
    }

    // Méthode pour vérifier si le token est encore valide
    public boolean isTokenValide() {
        return !estValide && LocalDateTime.now().isBefore(dateExpiration);
    }
}
