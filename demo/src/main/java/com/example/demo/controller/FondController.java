package com.example.demo.controller;

import com.example.demo.entity.Fond;
import com.example.demo.entity.Portefeuille;
import com.example.demo.repository.FondRepository;
import com.example.demo.repository.PortefeuilleRepository;
import com.example.demo.security.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/fonds")
public class FondController {

    @Autowired
    private PortefeuilleRepository portefeuilleRepository;

    @Autowired
    private FondRepository fondRepository;

    @Autowired
    private JwtTokenService jwtTokenService;

    @PostMapping("/depot")
    public ResponseEntity<?> faireDepot(
        @RequestHeader("Authorization") String token,
        @RequestBody Map<String, String> depotRequest
    ) {
        // Extraire et valider le token
        String actualToken = token.substring(7);
        
        if (!jwtTokenService.validateToken(actualToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token invalide");
        }

        // Récupérer l'ID de l'utilisateur
        Long userId = jwtTokenService.getUserIdFromToken(actualToken);

        // Vérifier le montant
        BigDecimal montant;
        try {
            montant = new BigDecimal(depotRequest.get("montant"));
            if (montant.compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest().body("Le montant doit être positif");
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Montant invalide");
        }

        // Rechercher le portefeuille de l'utilisateur
        Optional<Portefeuille> portefeuille = portefeuilleRepository.findByUtilisateurId(userId);
        
        if (portefeuille.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Aucun portefeuille trouvé");
        }

        // Rechercher ou créer le fond
        Fond fond = fondRepository.findByPortefeuille(portefeuille.get())
            .orElse(new Fond(portefeuille.get()));

        // Effectuer le dépôt
        fond.deposer(montant);
        fondRepository.save(fond);

        // Préparer la réponse
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Dépôt effectué avec succès");
        response.put("nouveauSolde", fond.getMontantActuel());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/retrait")
    public ResponseEntity<?> faireRetrait(
        @RequestHeader("Authorization") String token,
        @RequestBody Map<String, String> retraitRequest
    ) {
        // Extraire et valider le token
        String actualToken = token.substring(7);
        
        if (!jwtTokenService.validateToken(actualToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token invalide");
        }

        // Récupérer l'ID de l'utilisateur
        Long userId = jwtTokenService.getUserIdFromToken(actualToken);

        // Vérifier le montant
        BigDecimal montant;
        try {
            montant = new BigDecimal(retraitRequest.get("montant"));
            if (montant.compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest().body("Le montant doit être positif");
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Montant invalide");
        }

        // Rechercher le portefeuille de l'utilisateur
        Optional<Portefeuille> portefeuille = portefeuilleRepository.findByUtilisateurId(userId);
        
        if (portefeuille.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Aucun portefeuille trouvé");
        }

        // Rechercher le fond
        Optional<Fond> fondOptional = fondRepository.findByPortefeuille(portefeuille.get());
        
        if (fondOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Aucun fond trouvé");
        }

        Fond fond = fondOptional.get();

        try {
            // Effectuer le retrait
            fond.retirer(montant);
            fondRepository.save(fond);

            // Préparer la réponse
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Retrait effectué avec succès");
            response.put("nouveauSolde", fond.getMontantActuel());

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/solde")
    public ResponseEntity<?> consulterSolde(@RequestHeader("Authorization") String token) {
        // Extraire et valider le token
        String actualToken = token.substring(7);
        
        if (!jwtTokenService.validateToken(actualToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token invalide");
        }

        // Récupérer l'ID de l'utilisateur
        Long userId = jwtTokenService.getUserIdFromToken(actualToken);

        // Rechercher le portefeuille de l'utilisateur
        Optional<Portefeuille> portefeuille = portefeuilleRepository.findByUtilisateurId(userId);
        
        if (portefeuille.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Aucun portefeuille trouvé");
        }

        // Rechercher le fond
        Optional<Fond> fondOptional = fondRepository.findByPortefeuille(portefeuille.get());
        
        if (fondOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Aucun fond trouvé");
        }

        Fond fond = fondOptional.get();

        // Préparer la réponse
        Map<String, Object> response = new HashMap<>();
        response.put("solde", fond.getMontantActuel());

        return ResponseEntity.ok(response);
    }
}
