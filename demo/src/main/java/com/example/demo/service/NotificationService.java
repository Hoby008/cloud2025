package com.example.demo.service;

import com.example.demo.entity.Notification;
import com.example.demo.entity.ValidationUser;
import com.example.demo.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EmailService emailService;

    // Envoyer une notification à un utilisateur
    @Transactional
    public void envoyerNotification(ValidationUser utilisateur, String sujet, String message) {
        // Créer une notification
        Notification notification = new Notification();
        notification.setUtilisateur(utilisateur);
        notification.setSujet(sujet);
        notification.setMessage(message);
        notification.setDateCreation(LocalDateTime.now());
        notification.setLue(false);

        // Sauvegarder la notification
        notificationRepository.save(notification);

        // Envoyer un email si l'utilisateur a activé les notifications par email
        try {
            emailService.envoyerEmail(
                utilisateur.getEmail(), 
                sujet, 
                message
            );
        } catch (Exception e) {
            // Log de l'erreur sans interrompre le processus
            System.err.println("Erreur lors de l'envoi de l'email de notification : " + e.getMessage());
        }
    }

    // Récupérer les notifications non lues d'un utilisateur
    public List<Notification> getNotificationsNonLues(ValidationUser utilisateur) {
        return notificationRepository.findByUtilisateurAndLueFalseOrderByDateCreationDesc(utilisateur);
    }

    // Marquer une notification comme lue
    @Transactional
    public void marquerNotificationCommeLue(Long notificationId, ValidationUser utilisateur) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new IllegalArgumentException("Notification non trouvée"));

        // Vérifier que la notification appartient à l'utilisateur
        if (!notification.getUtilisateur().getId().equals(utilisateur.getId())) {
            throw new IllegalAccessError("Vous n'avez pas le droit de modifier cette notification");
        }

        notification.setLue(true);
        notificationRepository.save(notification);
    }

    // Supprimer les notifications lues
    @Transactional
    public void supprimerNotificationsLues(ValidationUser utilisateur) {
        notificationRepository.deleteByUtilisateurAndLueTrue(utilisateur);
    }
}
