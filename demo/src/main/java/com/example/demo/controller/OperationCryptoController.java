package com.example.demo.controller;

import com.example.demo.dto.OperationCryptoRequestDTO;
import com.example.demo.dto.OperationCryptoResponseDTO;
import com.example.demo.entity.OperationFaitCrypto;
import com.example.demo.entity.ValidationUser;
import com.example.demo.service.OperationCryptoService;
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
@RequestMapping("/api/operations-crypto")
public class OperationCryptoController {

    @Autowired
    private OperationCryptoService operationCryptoService;

    @Autowired
    private UserService userService;

    @GetMapping("/cryptos-disponibles")
    public ResponseEntity<?> getCryptosDisponibles() {
        // Récupérer la liste des cryptos disponibles
        return ResponseEntity.ok(
            Map.of("cryptos", List.of(
                "Bitcoin", "Ethereum", "Litecoin", "Cardano", "Solana", 
                "Ripple", "Polkadot", "Dogecoin", "Chainlink", "Stellar"
            ))
        );
    }

    @PostMapping("/effectuer")
    public ResponseEntity<?> effectuerOperationCrypto(
        @RequestBody OperationCryptoRequestDTO requete
    ) {
        try {
            // Récupérer l'utilisateur connecté
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            ValidationUser utilisateur = userService.findByUsername(username);

            // Effectuer l'opération
            OperationCryptoResponseDTO resultat = 
                operationCryptoService.effectuerOperationCrypto(utilisateur, requete);
            
            return ResponseEntity.ok(resultat);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
            );
        }
    }

    @GetMapping("/historique")
    public ResponseEntity<?> getHistoriqueOperations() {
        // Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ValidationUser utilisateur = userService.findByUsername(username);

        // Récupérer l'historique des opérations
        List<OperationFaitCrypto> operations = 
            operationCryptoService.getHistoriqueOperations(utilisateur);

        // Transformer en liste de réponses
        List<Map<String, Object>> historiqueReponse = operations.stream()
            .map(op -> Map.of(
                "typeCrypto", op.getTypeCrypto().getNom(),
                "typeOperation", op.getOperationCrypto().getNom(),
                "nombre", op.getNombre(),
                "prixFinal", op.getPrixFinal(),
                "commission", op.getCommission(),
                "dateHeure", op.getDateHeure()
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(historiqueReponse);
    }
}
