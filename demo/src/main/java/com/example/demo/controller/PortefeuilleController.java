package com.example.demo.controller;

import com.example.demo.entity.Fond;
import com.example.demo.entity.Portefeuille;
import com.example.demo.entity.ValidationUser;
import com.example.demo.repository.FondRepository;
import com.example.demo.repository.PortefeuilleRepository;
import com.example.demo.repository.ValidationUserRepository;
import com.example.demo.security.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/portefeuille")
public class PortefeuilleController {

    @Autowired
    private PortefeuilleRepository portefeuilleRepository;

    @Autowired
    private ValidationUserRepository validationUserRepository;

    @Autowired
    private FondRepository fondRepository;

    @Autowired
    private JwtTokenService jwtTokenService;

    @PostMapping("/creer")
    public ResponseEntity<?> creerPortefeuille(@RequestHeader("Authorization") String token) {
        // Extraire le token et vérifier sa validité
        String actualToken = token.substring(7);
        
        if (!jwtTokenService.validateToken(actualToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token invalide");
        }

        // Récupérer l'ID et l'email de l'utilisateur à partir du token
        Long userId = jwtTokenService.getUserIdFromToken(actualToken);
        String role = jwtTokenService.getRoleFromToken(actualToken);

        // Vérifier que l'utilisateur n'est pas un admin
        if ("ROLE_ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Les administrateurs ne peuvent pas créer de portefeuille");
        }

        // Vérifier si un portefeuille existe déjà
        Optional<Portefeuille> portefeuilleExistant = 
            portefeuilleRepository.findByUtilisateurId(userId);
        
        if (portefeuilleExistant.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Un portefeuille existe déjà pour cet utilisateur");
        }

        // Récupérer l'utilisateur
        Optional<ValidationUser> utilisateur = validationUserRepository.findById(userId);
        
        if (utilisateur.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Utilisateur non trouvé");
        }

        // Créer le portefeuille
        Portefeuille nouveauPortefeuille = new Portefeuille(utilisateur.get());
        Portefeuille portefeuilleEnregistre = portefeuilleRepository.save(nouveauPortefeuille);

        // Créer un fond associé au portefeuille
        Fond nouveauFond = new Fond(portefeuilleEnregistre);
        fondRepository.save(nouveauFond);

        // Préparer la réponse
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Portefeuille créé avec succès");
        response.put("portefeuilleId", portefeuilleEnregistre.getId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    public ResponseEntity<?> getPortefeuilleInfo(@RequestHeader("Authorization") String token) {
        // Extraire le token et vérifier sa validité
        String actualToken = token.substring(7);
        
        if (!jwtTokenService.validateToken(actualToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token invalide");
        }

        // Récupérer l'ID de l'utilisateur à partir du token
        Long userId = jwtTokenService.getUserIdFromToken(actualToken);
        String role = jwtTokenService.getRoleFromToken(actualToken);

        // Vérifier que l'utilisateur n'est pas un admin
        if ("ROLE_ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Les administrateurs ne peuvent pas consulter les portefeuilles");
        }

        // Rechercher le portefeuille
        Optional<Portefeuille> portefeuille = 
            portefeuilleRepository.findByUtilisateurId(userId);
        
        if (portefeuille.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Aucun portefeuille trouvé pour cet utilisateur");
        }

        // Rechercher le fond associé
        Optional<Fond> fond = fondRepository.findByPortefeuille(portefeuille.get());

        // Préparer la réponse
        Map<String, Object> response = new HashMap<>();
        response.put("portefeuilleId", portefeuille.get().getId());
        response.put("utilisateurId", userId);
        response.put("solde", fond.map(f -> f.getMontantActuel()).orElse(null));

        return ResponseEntity.ok(response);
    }
}
