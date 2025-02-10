package com.example.demo.service;

import com.example.demo.dto.OperationCryptoRequestDTO;
import com.example.demo.dto.OperationCryptoResponseDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.PreferenceCryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OperationCryptoService {

    @Autowired
    private ValidationUserRepository utilisateurRepository;

    @Autowired
    private TypeCryptoRepository typeCryptoRepository;

    @Autowired
    private OperationCryptoRepository operationCryptoRepository;

    @Autowired
    private CoursCryptomonnaieRepository coursCryptomonnaieRepository;

    @Autowired
    private OperationFaitCryptoRepository operationFaitCryptoRepository;

    @Autowired
    private CommissionRepository commissionRepository;

    @Autowired
    private PreferenceCryptoService preferenceCryptoService;

    @Transactional
    public OperationCryptoResponseDTO effectuerOperationCrypto(
        ValidationUser utilisateur, 
        OperationCryptoRequestDTO requete
    ) {
        // Valider la requête
        if (!requete.isValid()) {
            throw new IllegalArgumentException("Paramètres d'opération invalides");
        }

        // Récupérer le type de crypto
        TypeCrypto typeCrypto = typeCryptoRepository.findByNom(requete.getTypeCrypto())
            .orElseThrow(() -> new IllegalArgumentException("Crypto non trouvée"));

        // Récupérer le type d'opération
        OperationCrypto operationCrypto = operationCryptoRepository.findByNom(requete.getTypeOperation())
            .orElseThrow(() -> new IllegalArgumentException("Type d'opération non trouvé"));

        // Récupérer le dernier cours de la crypto
        List<CoursCryptomonnaie> coursCryptos = coursCryptomonnaieRepository
            .findByTypeCryptoAndDateHeureBetweenOrderByDateHeureAsc(
                typeCrypto, 
                LocalDateTime.now().minusHours(1), 
                LocalDateTime.now()
            );

        if (coursCryptos.isEmpty()) {
            throw new IllegalStateException("Impossible de récupérer le cours de la crypto");
        }

        CoursCryptomonnaie dernierCours = coursCryptos.get(coursCryptos.size() - 1);
        BigDecimal prixCrypto = dernierCours.getPrix();

        // Récupérer le taux de commission
        Commission commission = commissionRepository.findByOperationCryptoAndTypeCrypto(
            operationCrypto, typeCrypto
        ).orElseThrow(() -> new IllegalStateException("Commission non trouvée"));

        BigDecimal tauxCommission = commission.getTauxCommission();

        // Calculer l'opération
        BigDecimal nombreCrypto;
        BigDecimal montantTotal;
        BigDecimal montantCommission;

        if ("Achat".equals(requete.getTypeOperation())) {
            // Vérifier le solde de l'utilisateur
            if (utilisateur.getSolde().compareTo(requete.getMontant()) < 0) {
                throw new IllegalArgumentException("Solde insuffisant");
            }

            // Calculer le nombre de crypto et la commission
            nombreCrypto = requete.getMontant().divide(prixCrypto, 8, RoundingMode.HALF_UP);
            montantCommission = requete.getMontant().multiply(tauxCommission)
                .setScale(2, RoundingMode.HALF_UP);
            montantTotal = requete.getMontant().add(montantCommission);

            // Mettre à jour le solde
            utilisateur.setSolde(utilisateur.getSolde().subtract(montantTotal));
        } else { // Vente
            // Vérifier la quantité de crypto disponible
            Optional<OperationFaitCrypto> operationExistante = 
                operationFaitCryptoRepository.findByUtilisateurAndTypeCrypto(utilisateur, typeCrypto)
                .stream()
                .filter(op -> op.getOperationCrypto().getNom().equals("Achat"))
                .findFirst();

            if (operationExistante.isEmpty() || 
                operationExistante.get().getNombre().compareTo(requete.getMontant()) < 0) {
                throw new IllegalArgumentException("Quantité de crypto insuffisante");
            }

            // Calculer le montant total et la commission
            nombreCrypto = requete.getMontant();
            BigDecimal montantVente = nombreCrypto.multiply(prixCrypto);
            montantCommission = montantVente.multiply(tauxCommission)
                .setScale(2, RoundingMode.HALF_UP);
            montantTotal = montantVente.subtract(montantCommission);

            // Mettre à jour le solde
            utilisateur.setSolde(utilisateur.getSolde().add(montantTotal));
        }

        // Sauvegarder l'utilisateur
        utilisateurRepository.save(utilisateur);

        // Créer l'opération
        OperationFaitCrypto operationFaitCrypto = new OperationFaitCrypto(
            operationCrypto, 
            typeCrypto, 
            montantCommission, 
            nombreCrypto, 
            prixCrypto, 
            utilisateur
        );

        // Sauvegarder l'opération
        operationFaitCryptoRepository.save(operationFaitCrypto);

        // Vérifier et envoyer une notification si nécessaire
        preferenceCryptoService.verifierNotificationCrypto(
            utilisateur, 
            typeCrypto, 
            operationCrypto.getNom()
        );

        // Préparer la réponse
        return new OperationCryptoResponseDTO(
            typeCrypto.getNom(),
            operationCrypto.getNom(),
            requete.getMontant(),
            montantCommission,
            prixCrypto,
            nombreCrypto,
            LocalDateTime.now()
        );
    }

    // Méthode pour récupérer l'historique des opérations d'un utilisateur
    public List<OperationFaitCrypto> getHistoriqueOperations(ValidationUser utilisateur) {
        return operationFaitCryptoRepository.findByUtilisateur(utilisateur);
    }
}
