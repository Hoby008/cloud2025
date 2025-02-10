package com.example.demo.service;

import com.example.demo.dto.OperationDepotRetraitRequestDTO;
import com.example.demo.entity.ValidationUser;
import com.example.demo.repository.ValidationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class DepotRetraitService {

    @Autowired
    private ValidationUserRepository utilisateurRepository;

    @Transactional
    public void effectuerDepotRetrait(
        ValidationUser utilisateur, 
        OperationDepotRetraitRequestDTO requete
    ) {
        // Valider la requête
        if (!requete.isValid()) {
            throw new IllegalArgumentException("Paramètres d'opération invalides");
        }

        // Effectuer le dépôt ou le retrait
        if ("Depot".equals(requete.getTypeOperation())) {
            // Ajouter le montant au solde
            utilisateur.setSolde(
                utilisateur.getSolde().add(requete.getMontant())
            );
        } else { // Retrait
            // Vérifier le solde suffisant
            if (utilisateur.getSolde().compareTo(requete.getMontant()) < 0) {
                throw new IllegalArgumentException("Solde insuffisant");
            }

            // Soustraire le montant du solde
            utilisateur.setSolde(
                utilisateur.getSolde().subtract(requete.getMontant())
            );
        }

        // Sauvegarder l'utilisateur
        utilisateurRepository.save(utilisateur);
    }
}
