package com.example.demo.controller;

import com.example.demo.entity.ValidationUser;
import com.example.demo.repository.ValidationUserRepository;
import com.example.demo.security.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private ValidationUserRepository validationUserRepository;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Map pour stocker les tokens révoqués
    private Map<String, Boolean> revokedTokens = new HashMap<>();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        List<ValidationUser> users = validationUserRepository.findByEmail(email);
        
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Utilisateur non trouvé");
        }

        ValidationUser user = users.get(0);

        // Vérifier si l'utilisateur est totalement inscrit
        if (!user.getEstTotalementInscrit()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Inscription non finalisée. Veuillez valider votre compte.");
        }

        // Vérifier le mot de passe
        if (!passwordEncoder.matches(password, user.getPassword())) {
            // Incrémenter le nombre de tentatives
            user.setNumeroTentative(user.getNumeroTentative() + 1);
            
            if (user.getNumeroTentative() >= 3) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Nombre maximum de tentatives atteint. Compte bloqué.");
            }
            
            validationUserRepository.save(user);
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Mot de passe incorrect");
        }

        // Réinitialiser le nombre de tentatives
        user.setNumeroTentative(1);
        validationUserRepository.save(user);

        // Générer le token
        String token = jwtTokenService.generateToken(user);

        return ResponseEntity.ok(Map.of(
            "token", token,
            "userId", user.getId(),
            "role", user.getRoleId() == 1 ? "ADMIN" : "USER"
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        // Supprimer le "Bearer " du token
        String actualToken = token.substring(7);
        
        // Ajouter le token à la liste des tokens révoqués
        revokedTokens.put(actualToken, true);

        return ResponseEntity.ok("Déconnexion réussie");
    }

    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestBody Map<String, String> tokenRequest) {
        String token = tokenRequest.get("token");

        // Vérifier si le token est révoqué
        if (revokedTokens.containsKey(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token révoqué");
        }

        // Valider le token
        if (jwtTokenService.validateToken(token)) {
            Long userId = jwtTokenService.getUserIdFromToken(token);
            String role = jwtTokenService.getRoleFromToken(token);

            return ResponseEntity.ok(Map.of(
                "valid", true,
                "userId", userId,
                "role", role
            ));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("Token invalide");
    }
}
