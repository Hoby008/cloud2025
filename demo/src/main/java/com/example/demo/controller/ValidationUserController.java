package com.example.demo.controller;

import com.example.demo.entity.ValidationUser;
import com.example.demo.repository.ValidationUserRepository;
import com.example.demo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class ValidationUserController {

    @Autowired
    private ValidationUserRepository validationUserRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody ValidationUser user) {
        // Forcer le rôle utilisateur
        user.setRoleId(2);

        // Vérifier si l'email existe déjà
        List<ValidationUser> existingUsers = validationUserRepository.findByEmail(user.getEmail());
        
        if (!existingUsers.isEmpty()) {
            // Vérifier le nombre de tentatives
            ValidationUser existingUser = existingUsers.get(0);
            if (existingUser.getNumeroTentative() >= 3) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Nombre maximum de tentatives atteint pour cet email");
            }
            
            // Mettre à jour l'utilisateur existant
            existingUser.setNom(user.getNom());
            existingUser.setPrenom(user.getPrenom());
            existingUser.setPassword(user.getPassword());
            existingUser.genererCodeValidation();
            existingUser.setNumeroTentative(existingUser.getNumeroTentative() + 1);
            existingUser.setRoleId(2); // Forcer le rôle utilisateur
            
            validationUserRepository.save(existingUser);
            
            return ResponseEntity.ok(Map.of(
                "message", "Code de validation envoyé",
                "codeValidation", existingUser.getCodeValidation(),
                "expirationTime", existingUser.getDateExpiration()
            ));
        }
        
        // Nouvel utilisateur
        user.genererCodeValidation();
        user.setNumeroTentative(1);
        user.setEtatValidation(false);
        user.setEstTotalementInscrit(false);
        user.setRoleId(2); // Forcer le rôle utilisateur
        
        ValidationUser savedUser = validationUserRepository.save(user);
        
        return ResponseEntity.ok(Map.of(
            "message", "Code de validation envoyé",
            "codeValidation", savedUser.getCodeValidation(),
            "expirationTime", savedUser.getDateExpiration()
        ));
    }

    @PostMapping("/validate-pin")
    public ResponseEntity<?> validatePin(@RequestBody Map<String, String> validationRequest) {
        String email = validationRequest.get("email");
        String codeEntre = validationRequest.get("codeValidation");

        // Rechercher l'utilisateur par email
        List<ValidationUser> users = validationUserRepository.findByEmail(email);
        
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Utilisateur non trouvé");
        }

        ValidationUser user = users.get(0);

        // Vérifier si le compte est bloqué
        if (user.getEstBloque()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Votre compte est bloqué. Veuillez consulter votre email pour le débloquer.");
        }

        // Tenter de valider le code PIN
        boolean validationReussie = user.validerCodePin(codeEntre);

        // Sauvegarder les modifications
        validationUserRepository.save(user);

        if (validationReussie) {
            return ResponseEntity.ok("Validation réussie");
        } else {
            // Vérifier si le compte est maintenant bloqué
            if (user.getEstBloque()) {
                // Envoyer un email de déblocage
                emailService.envoyerEmailDeblocage(user.getEmail(), user.getTokenDeblocage());
                
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Trop de tentatives. Un email de déblocage a été envoyé.");
            }
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Code de validation incorrect");
        }
    }
}
