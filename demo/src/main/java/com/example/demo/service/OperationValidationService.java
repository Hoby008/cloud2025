package com.example.demo.service;

import com.example.demo.dto.OperationCryptoRequestDTO;
import com.example.demo.dto.OperationCryptoResponseDTO;
import com.example.demo.dto.OperationDepotRetraitRequestDTO;
import com.example.demo.entity.OperationValidation;
import com.example.demo.entity.ValidationUser;
import com.example.demo.repository.OperationValidationRepository;
import com.example.demo.repository.ValidationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OperationValidationService {

    @Autowired
    private OperationValidationRepository operationValidationRepository;

    @Autowired
    private ValidationUserRepository utilisateurRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OperationCryptoService operationCryptoService;

    @Autowired
    private DepotRetraitService depotRetraitService;

    // Créer une validation pour une opération de crypto
    @Transactional
    public OperationValidation creerValidationOperationCrypto(
        ValidationUser utilisateur, 
        OperationCryptoRequestDTO requete
    ) {
        // Convertir les détails en chaîne JSON
        String detailsOperation = String.format(
            "{\"typeCrypto\":\"%s\",\"typeOperation\":\"%s\",\"montant\":%s}", 
            requete.getTypeCrypto(), 
            requete.getTypeOperation(), 
            requete.getMontant()
        );

        // Créer la validation
        OperationValidation validation = new OperationValidation(
            utilisateur, 
            requete.getTypeOperation() + " Crypto", 
            detailsOperation
        );

        // Sauvegarder la validation
        operationValidationRepository.save(validation);

        // Envoyer l'email de validation
        envoyerEmailValidation(utilisateur, validation);

        return validation;
    }

    // Créer une validation pour un dépôt/retrait
    @Transactional
    public OperationValidation creerValidationDepotRetrait(
        ValidationUser utilisateur, 
        OperationDepotRetraitRequestDTO requete
    ) {
        // Convertir les détails en chaîne JSON
        String detailsOperation = String.format(
            "{\"typeOperation\":\"%s\",\"montant\":%s}", 
            requete.getTypeOperation(), 
            requete.getMontant()
        );

        // Créer la validation
        OperationValidation validation = new OperationValidation(
            utilisateur, 
            requete.getTypeOperation(), 
            detailsOperation
        );

        // Sauvegarder la validation
        operationValidationRepository.save(validation);

        // Envoyer l'email de validation
        envoyerEmailValidation(utilisateur, validation);

        return validation;
    }

    // Envoyer un email de validation
    private void envoyerEmailValidation(ValidationUser utilisateur, OperationValidation validation) {
        String sujet = "Validation de votre opération";
        String corps = String.format(
            "Bonjour %s,\n\n" +
            "Vous avez demandé à effectuer une opération de type %s.\n" +
            "Pour valider cette opération, cliquez sur le lien suivant :\n" +
            "http://votre-plateforme.com/valider-operation?token=%s\n\n" +
            "Ce lien expirera dans 1 heure.\n\n" +
            "Cordialement,\nVotre équipe de support",
            utilisateur.getUsername(),
            validation.getTypeOperation(),
            validation.getToken()
        );

        emailService.envoyerEmail(utilisateur.getEmail(), sujet, corps);
    }

    // Valider une opération
    @Transactional
    public boolean validerOperation(String token) {
        // Rechercher la validation
        Optional<OperationValidation> validationOpt = 
            operationValidationRepository.findByToken(token);

        if (validationOpt.isEmpty()) {
            throw new IllegalArgumentException("Token de validation invalide");
        }

        OperationValidation validation = validationOpt.get();

        // Vérifier si le token est encore valide
        if (!validation.isTokenValide()) {
            throw new IllegalStateException("Le token de validation a expiré");
        }

        // Marquer comme validé
        validation.setEstValide(true);
        operationValidationRepository.save(validation);

        // Exécuter l'opération en fonction du type
        switch (validation.getTypeOperation()) {
            case "Achat Crypto":
            case "Vente Crypto":
                return executerOperationCrypto(validation);
            case "Depot":
            case "Retrait":
                return executerDepotRetrait(validation);
            default:
                throw new IllegalArgumentException("Type d'opération non reconnu");
        }
    }

    // Exécuter une opération de crypto
    private boolean executerOperationCrypto(OperationValidation validation) {
        try {
            // Convertir les détails JSON
            OperationCryptoRequestDTO requete = convertirDetailsCrypto(validation);

            // Effectuer l'opération
            operationCryptoService.effectuerOperationCrypto(
                validation.getUtilisateur(), 
                requete
            );

            return true;
        } catch (Exception e) {
            // Gérer les erreurs d'exécution
            validation.setEstValide(false);
            operationValidationRepository.save(validation);
            return false;
        }
    }

    // Exécuter un dépôt/retrait
    private boolean executerDepotRetrait(OperationValidation validation) {
        try {
            // Convertir les détails JSON
            OperationDepotRetraitRequestDTO requete = convertirDetailsDepotRetrait(validation);

            // Effectuer l'opération
            depotRetraitService.effectuerDepotRetrait(
                validation.getUtilisateur(), 
                requete
            );

            return true;
        } catch (Exception e) {
            // Gérer les erreurs d'exécution
            validation.setEstValide(false);
            operationValidationRepository.save(validation);
            return false;
        }
    }

    // Convertir les détails JSON en DTO pour opération crypto
    private OperationCryptoRequestDTO convertirDetailsCrypto(OperationValidation validation) {
        // Utiliser une bibliothèque de parsing JSON comme Jackson
        // Ici, pour simplifier, on utilise un parsing manuel
        String details = validation.getDetailsOperation();
        String typeCrypto = extraireValeur(details, "typeCrypto");
        String typeOperation = extraireValeur(details, "typeOperation");
        String montantStr = extraireValeur(details, "montant");

        return new OperationCryptoRequestDTO(
            typeCrypto.replace("\"", ""),
            typeOperation.replace("\"", ""),
            java.math.BigDecimal.valueOf(Double.parseDouble(montantStr))
        );
    }

    // Convertir les détails JSON en DTO pour dépôt/retrait
    private OperationDepotRetraitRequestDTO convertirDetailsDepotRetrait(OperationValidation validation) {
        String details = validation.getDetailsOperation();
        String typeOperation = extraireValeur(details, "typeOperation");
        String montantStr = extraireValeur(details, "montant");

        return new OperationDepotRetraitRequestDTO(
            typeOperation.replace("\"", ""),
            java.math.BigDecimal.valueOf(Double.parseDouble(montantStr))
        );
    }

    // Méthode utilitaire pour extraire une valeur d'une chaîne JSON simple
    private String extraireValeur(String json, String cle) {
        int debutIndex = json.indexOf("\"" + cle + "\":") + cle.length() + 3;
        int finIndex = json.indexOf(",", debutIndex);
        if (finIndex == -1) {
            finIndex = json.indexOf("}", debutIndex);
        }
        return json.substring(debutIndex, finIndex);
    }

    // Nettoyer les validations expirées
    @Scheduled(fixedRate = 3600000) // Toutes les heures
    @Transactional
    public void nettoyerValidationsExpirees() {
        operationValidationRepository.deleteByDateExpirationBefore(LocalDateTime.now());
    }

    // Récupérer les validations en attente pour un utilisateur
    public List<OperationValidation> getValidationsEnAttente(ValidationUser utilisateur) {
        return operationValidationRepository.findByUtilisateurAndEstValideAndDateExpirationAfter(
            utilisateur, 
            false, 
            LocalDateTime.now()
        );
    }
}
