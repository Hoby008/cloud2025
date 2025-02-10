package com.example.demo.controller;

import com.example.demo.entity.Notification;
import com.example.demo.entity.TypeCrypto;
import com.example.demo.entity.ValidationUser;
import com.example.demo.service.NotificationService;
import com.example.demo.service.PreferenceCryptoService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/crypto-preference")
public class PreferenceCryptoController {

    @Autowired
    private PreferenceCryptoService preferenceCryptoService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    // Récupérer les types de crypto disponibles
    @GetMapping("/types-disponibles")
    public ResponseEntity<?> getTypesCryptoDisponibles() {
        List<TypeCrypto> typesCrypto = preferenceCryptoService.getTypesCryptoDisponibles();
        
        return ResponseEntity.ok(
            typesCrypto.stream()
                .map(crypto -> Map.of(
                    "id", crypto.getId(),
                    "nom", crypto.getNom(),
                    "symbole", crypto.getSymbole()
                ))
                .collect(Collectors.toList())
        );
    }

    // Définir la crypto préférée
    @PostMapping("/definir")
    public ResponseEntity<?> definirCryptoPreferee(@RequestBody Map<String, Long> request) {
        // Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ValidationUser utilisateur = userService.findByUsername(username);

        // Définir la crypto préférée
        Long cryptoId = request.get("cryptoId");
        preferenceCryptoService.definirCryptoPreferee(utilisateur, cryptoId);

        return ResponseEntity.ok(Map.of("message", "Crypto préférée mise à jour avec succès"));
    }

    // Activer/désactiver les notifications pour la crypto préférée
    @PostMapping("/notifications")
    public ResponseEntity<?> toggleNotificationsCrypto(@RequestBody Map<String, Boolean> request) {
        // Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ValidationUser utilisateur = userService.findByUsername(username);

        // Activer/désactiver les notifications
        Boolean activer = request.get("activer");
        preferenceCryptoService.toggleNotificationsCrypto(utilisateur, activer);

        return ResponseEntity.ok(
            Map.of(
                "message", activer ? 
                    "Notifications activées avec succès" : 
                    "Notifications désactivées avec succès"
            )
        );
    }

    // Récupérer les notifications non lues
    @GetMapping("/notifications")
    public ResponseEntity<?> getNotificationsNonLues() {
        // Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ValidationUser utilisateur = userService.findByUsername(username);

        // Récupérer les notifications non lues
        List<Notification> notifications = notificationService.getNotificationsNonLues(utilisateur);

        return ResponseEntity.ok(
            notifications.stream()
                .map(notif -> Map.of(
                    "id", notif.getId(),
                    "sujet", notif.getSujet(),
                    "message", notif.getMessage(),
                    "dateCreation", notif.getDateCreation()
                ))
                .collect(Collectors.toList())
        );
    }

    // Marquer une notification comme lue
    @PostMapping("/notifications/marquer-lue")
    public ResponseEntity<?> marquerNotificationCommeLue(@RequestBody Map<String, Long> request) {
        // Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ValidationUser utilisateur = userService.findByUsername(username);

        // Marquer la notification comme lue
        Long notificationId = request.get("notificationId");
        notificationService.marquerNotificationCommeLue(notificationId, utilisateur);

        return ResponseEntity.ok(Map.of("message", "Notification marquée comme lue"));
    }

    // Supprimer les notifications lues
    @DeleteMapping("/notifications")
    public ResponseEntity<?> supprimerNotificationsLues() {
        // Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ValidationUser utilisateur = userService.findByUsername(username);

        // Supprimer les notifications lues
        notificationService.supprimerNotificationsLues(utilisateur);

        return ResponseEntity.ok(Map.of("message", "Notifications lues supprimées avec succès"));
    }
}
