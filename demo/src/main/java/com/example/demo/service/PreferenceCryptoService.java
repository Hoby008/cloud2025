package com.example.demo.service;

import com.example.demo.entity.TypeCrypto;
import com.example.demo.entity.ValidationUser;
import com.example.demo.repository.TypeCryptoRepository;
import com.example.demo.repository.ValidationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PreferenceCryptoService {

    @Autowired
    private ValidationUserRepository utilisateurRepository;

    @Autowired
    private TypeCryptoRepository typeCryptoRepository;

    @Autowired
    private NotificationService notificationService;

    // Récupérer tous les types de crypto disponibles
    public List<TypeCrypto> getTypesCryptoDisponibles() {
        return typeCryptoRepository.findAll();
    }

    // Définir la crypto préférée
    @Transactional
    public void definirCryptoPreferee(ValidationUser utilisateur, Long cryptoId) {
        // Trouver le type de crypto
        TypeCrypto cryptoPreferee = typeCryptoRepository.findById(cryptoId)
            .orElseThrow(() -> new IllegalArgumentException("Type de crypto invalide"));

        // Mettre à jour la crypto préférée
        utilisateur.setCryptoPreferee(cryptoPreferee);
        utilisateurRepository.save(utilisateur);
    }

    // Activer/désactiver les notifications pour la crypto préférée
    @Transactional
    public void toggleNotificationsCrypto(ValidationUser utilisateur, boolean activer) {
        // Vérifier si une crypto préférée est définie
        if (utilisateur.getCryptoPreferee() == null) {
            throw new IllegalStateException("Aucune crypto préférée n'est définie");
        }

        // Mettre à jour le statut des notifications
        utilisateur.setNotificationsCryptoActives(activer);
        utilisateurRepository.save(utilisateur);
    }

    // Envoyer une notification si l'opération concerne la crypto préférée
    public void verifierNotificationCrypto(ValidationUser utilisateur, TypeCrypto typeCrypto, String typeOperation) {
        // Vérifier si les notifications sont activées et si la crypto correspond
        if (Boolean.TRUE.equals(utilisateur.getNotificationsCryptoActives()) && 
            utilisateur.getCryptoPreferee() != null && 
            utilisateur.getCryptoPreferee().getId().equals(typeCrypto.getId())) {
            
            // Préparer le message de notification
            String sujet = "Opération sur votre crypto préférée";
            String message = String.format(
                "Une opération %s a été réalisée sur votre crypto préférée %s", 
                typeOperation, 
                typeCrypto.getNom()
            );

            // Envoyer la notification
            notificationService.envoyerNotification(utilisateur, sujet, message);
        }
    }
}
