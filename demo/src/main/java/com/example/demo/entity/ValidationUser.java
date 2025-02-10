package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "validation_user")
public class ValidationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_id", nullable = false)
    private Integer roleId = 2; // Par défaut, tous les nouveaux utilisateurs sont des utilisateurs

    @Column(name = "nom", length = 100, nullable = false)
    private String nom;

    @Column(name = "prenom", length = 100, nullable = false)
    private String prenom;

    @Column(name = "email", length = 255, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "code_validation", length = 6)
    private String codeValidation;

    @Column(name = "date_expiration")
    private LocalDateTime dateExpiration;

    @Column(name = "numero_tentative_validation", nullable = false)
    private Integer numeroTentativeValidation = 0;

    @Column(name = "etat_validation", nullable = false)
    private Boolean etatValidation = false;

    @Column(name = "est_totalement_inscrit", nullable = false)
    private Boolean estTotalementInscrit = false;

    @Column(name = "est_bloque", nullable = false)
    private Boolean estBloque = false;

    @Column(name = "token_deblocage")
    private String tokenDeblocage;

    @Column(name = "date_token_deblocage")
    private LocalDateTime dateTokenDeblocage;

    @Column(name = "photo_profil_path")
    private String photoProfilPath;

    @ManyToOne
    @JoinColumn(name = "crypto_preferee_id")
    private TypeCrypto cryptoPreferee;

    @Column(name = "notifications_crypto_actives")
    private Boolean notificationsCryptoActives = false;

    public ValidationUser() {}

    public ValidationUser(String nom, String prenom, String email, String password) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.roleId = 2; // Par défaut, utilisateur
        this.genererCodeValidation();
    }

    // Méthode pour générer un code de validation à 6 chiffres
    public void genererCodeValidation() {
        // Génère un code PIN aléatoire à 6 chiffres
        this.codeValidation = String.format("%06d", (int) (Math.random() * 1000000));
        
        // Le code expire dans 90 secondes
        this.dateExpiration = LocalDateTime.now().plusSeconds(90);
    }

    public void incrementerTentativesValidation() {
        this.numeroTentativeValidation++;
        if (this.roleId != 1 && this.numeroTentativeValidation >= 3) {
            this.estBloque = true;
            // Générer un token de déblocage unique
            this.tokenDeblocage = UUID.randomUUID().toString();
            this.dateTokenDeblocage = LocalDateTime.now().plusHours(24); // Valable 24h
        }
    }

    public boolean validerCodePin(String codeEntre) {
        // Vérifier si le compte est bloqué
        if (this.estBloque) {
            return false;
        }

        // Vérifier le code et l'expiration
        if (this.codeValidation.equals(codeEntre) && LocalDateTime.now().isBefore(this.dateExpiration)) {
            this.etatValidation = true;
            this.estTotalementInscrit = true;
            this.numeroTentativeValidation = 0; // Réinitialiser les tentatives
            return true;
        } else {
            incrementerTentativesValidation();
            return false;
        }
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCodeValidation() {
        return codeValidation;
    }

    public void setCodeValidation(String codeValidation) {
        this.codeValidation = codeValidation;
    }

    public LocalDateTime getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDateTime dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public Integer getNumeroTentativeValidation() {
        return numeroTentativeValidation;
    }

    public void setNumeroTentativeValidation(Integer numeroTentativeValidation) {
        this.numeroTentativeValidation = numeroTentativeValidation;
    }

    public Boolean getEtatValidation() {
        return etatValidation;
    }

    public void setEtatValidation(Boolean etatValidation) {
        this.etatValidation = etatValidation;
    }

    public Boolean getEstTotalementInscrit() {
        return estTotalementInscrit;
    }

    public void setEstTotalementInscrit(Boolean estTotalementInscrit) {
        this.estTotalementInscrit = estTotalementInscrit;
    }

    public Boolean getEstBloque() {
        return estBloque;
    }

    public void setEstBloque(Boolean estBloque) {
        this.estBloque = estBloque;
    }

    public String getTokenDeblocage() {
        return tokenDeblocage;
    }

    public void setTokenDeblocage(String tokenDeblocage) {
        this.tokenDeblocage = tokenDeblocage;
    }

    public LocalDateTime getDateTokenDeblocage() {
        return dateTokenDeblocage;
    }

    public void setDateTokenDeblocage(LocalDateTime dateTokenDeblocage) {
        this.dateTokenDeblocage = dateTokenDeblocage;
    }

    public String getPhotoProfilPath() {
        return photoProfilPath;
    }

    public void setPhotoProfilPath(String photoProfilPath) {
        this.photoProfilPath = photoProfilPath;
    }

    public TypeCrypto getCryptoPreferee() {
        return cryptoPreferee;
    }

    public void setCryptoPreferee(TypeCrypto cryptoPreferee) {
        this.cryptoPreferee = cryptoPreferee;
    }

    public Boolean getNotificationsCryptoActives() {
        return notificationsCryptoActives;
    }

    public void setNotificationsCryptoActives(Boolean notificationsCryptoActives) {
        this.notificationsCryptoActives = notificationsCryptoActives;
    }
}
