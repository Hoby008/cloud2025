package com.example.demo.controller;

import com.example.demo.entity.ValidationUser;
import com.example.demo.repository.ValidationUserRepository;
import com.example.demo.security.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    @Autowired
    private ValidationUserRepository validationUserRepository;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(
        @RequestHeader("Authorization") String token, 
        @RequestBody Map<String, String> updateRequest
    ) {
        // Extraire le token et vérifier sa validité
        String actualToken = token.substring(7);
        
        if (!jwtTokenService.validateToken(actualToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token invalide");
        }

        // Récupérer l'ID et l'email de l'utilisateur à partir du token
        Long userId = jwtTokenService.getUserIdFromToken(actualToken);
        String userEmail = jwtTokenService.getEmailFromToken(actualToken);

        // Rechercher l'utilisateur
        Optional<ValidationUser> userOptional = validationUserRepository.findById(userId);
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Utilisateur non trouvé");
        }

        ValidationUser user = userOptional.get();

        // Vérifier et mettre à jour les champs
        boolean updated = false;

        // Mise à jour du nom
        if (updateRequest.containsKey("nom") && !updateRequest.get("nom").isEmpty()) {
            user.setNom(updateRequest.get("nom"));
            updated = true;
        }

        // Mise à jour du prénom
        if (updateRequest.containsKey("prenom") && !updateRequest.get("prenom").isEmpty()) {
            user.setPrenom(updateRequest.get("prenom"));
            updated = true;
        }

        // Mise à jour du mot de passe
        if (updateRequest.containsKey("password") && !updateRequest.get("password").isEmpty()) {
            // Vérifier l'ancien mot de passe si fourni
            if (updateRequest.containsKey("oldPassword")) {
                if (!passwordEncoder.matches(updateRequest.get("oldPassword"), user.getPassword())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Ancien mot de passe incorrect");
                }
            }

            // Hacher le nouveau mot de passe
            user.setPassword(passwordEncoder.encode(updateRequest.get("password")));
            updated = true;
        }

        // Interdire la modification de l'email
        if (updateRequest.containsKey("email")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("La modification de l'email n'est pas autorisée");
        }

        // Sauvegarder les modifications si nécessaire
        if (updated) {
            validationUserRepository.save(user);
            return ResponseEntity.ok("Profil mis à jour avec succès");
        }

        return ResponseEntity.badRequest().body("Aucune modification effectuée");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String token) {
        // Extraire le token et vérifier sa validité
        String actualToken = token.substring(7);
        
        if (!jwtTokenService.validateToken(actualToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token invalide");
        }

        // Récupérer l'ID de l'utilisateur à partir du token
        Long userId = jwtTokenService.getUserIdFromToken(actualToken);

        // Rechercher l'utilisateur
        Optional<ValidationUser> userOptional = validationUserRepository.findById(userId);
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Utilisateur non trouvé");
        }

        ValidationUser user = userOptional.get();

        // Créer un DTO pour masquer le mot de passe
        AdminController.UserDTO userDTO = new AdminController.UserDTO(user);

        return ResponseEntity.ok(userDTO);
    }
}
