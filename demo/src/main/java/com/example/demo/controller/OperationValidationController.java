package com.example.demo.controller;

import com.example.demo.dto.OperationCryptoRequestDTO;
import com.example.demo.dto.OperationDepotRetraitRequestDTO;
import com.example.demo.entity.OperationValidation;
import com.example.demo.entity.ValidationUser;
import com.example.demo.service.OperationValidationService;
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
@RequestMapping("/api/operations")
public class OperationValidationController {

    @Autowired
    private OperationValidationService operationValidationService;

    @Autowired
    private UserService userService;

    // Demander une validation pour une opération de crypto
    @PostMapping("/crypto/demander-validation")
    public ResponseEntity<?> demanderValidationOperationCrypto(
        @RequestBody OperationCryptoRequestDTO requete
    ) {
        try {
            // Récupérer l'utilisateur connecté
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            ValidationUser utilisateur = userService.findByUsername(username);

            // Créer la validation
            OperationValidation validation = 
                operationValidationService.creerValidationOperationCrypto(utilisateur, requete);
            
            return ResponseEntity.ok(Map.of(
                "message", "Demande de validation envoyée par email",
                "token", validation.getToken()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
            );
        }
    }

    // Demander une validation pour un dépôt/retrait
    @PostMapping("/depot-retrait/demander-validation")
    public ResponseEntity<?> demanderValidationDepotRetrait(
        @RequestBody OperationDepotRetraitRequestDTO requete
    ) {
        try {
            // Récupérer l'utilisateur connecté
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            ValidationUser utilisateur = userService.findByUsername(username);

            // Créer la validation
            OperationValidation validation = 
                operationValidationService.creerValidationDepotRetrait(utilisateur, requete);
            
            return ResponseEntity.ok(Map.of(
                "message", "Demande de validation envoyée par email",
                "token", validation.getToken()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
            );
        }
    }

    // Valider une opération
    @GetMapping("/valider")
    public ResponseEntity<?> validerOperation(
        @RequestParam("token") String token
    ) {
        try {
            boolean operationReussie = operationValidationService.validerOperation(token);
            
            return ResponseEntity.ok(Map.of(
                "message", operationReussie ? 
                    "Opération validée et exécutée avec succès" : 
                    "Erreur lors de l'exécution de l'opération"
            ));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
            );
        }
    }

    // Récupérer les validations en attente
    @GetMapping("/validations-en-attente")
    public ResponseEntity<?> getValidationsEnAttente() {
        // Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ValidationUser utilisateur = userService.findByUsername(username);

        // Récupérer les validations en attente
        List<OperationValidation> validations = 
            operationValidationService.getValidationsEnAttente(utilisateur);

        // Transformer en liste de réponses
        List<Map<String, Object>> validationsReponse = validations.stream()
            .map(validation -> Map.of(
                "typeOperation", validation.getTypeOperation(),
                "dateCreation", validation.getDateCreation(),
                "dateExpiration", validation.getDateExpiration(),
                "detailsOperation", validation.getDetailsOperation()
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(validationsReponse);
    }
}
