package com.example.demo.controller;

import com.example.demo.dto.OperationFiltreRequestDTO;
import com.example.demo.entity.OperationFaitCrypto;
import com.example.demo.entity.OperationValidation;
import com.example.demo.entity.ValidationUser;
import com.example.demo.service.UserOperationService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/operations")
public class UserOperationController {

    @Autowired
    private UserOperationService userOperationService;

    @Autowired
    private UserService userService;

    // Récupérer les filtres disponibles
    @GetMapping("/filtres")
    public ResponseEntity<?> getFiltresDisponibles() {
        return ResponseEntity.ok(
            Map.of(
                "typesOperation", OperationFiltreRequestDTO.getTypesOperationPossibles(),
                "typesCrypto", OperationFiltreRequestDTO.getTypesCryptoPossibles(),
                "etatsValidation", OperationFiltreRequestDTO.getEtatsValidationPossibles()
            )
        );
    }

    // Rechercher des opérations de validation
    @PostMapping("/validations")
    public ResponseEntity<?> rechercherOperationsValidation(
        @RequestBody OperationFiltreRequestDTO filtres
    ) {
        // Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ValidationUser utilisateur = userService.findByUsername(username);

        // Rechercher les opérations de validation
        Page<OperationValidation> operationsValidation = 
            userOperationService.rechercherOperationsValidationUtilisateur(utilisateur, filtres);

        return ResponseEntity.ok(
            Map.of(
                "operations", operationsValidation.getContent().stream()
                    .map(op -> Map.of(
                        "id", op.getId(),
                        "typeOperation", op.getTypeOperation(),
                        "dateCreation", op.getDateCreation(),
                        "dateExpiration", op.getDateExpiration(),
                        "estValide", op.getEstValide(),
                        "detailsOperation", op.getDetailsOperation()
                    ))
                    .collect(Collectors.toList()),
                "totalPages", operationsValidation.getTotalPages(),
                "totalElements", operationsValidation.getTotalElements(),
                "pageActuelle", operationsValidation.getNumber()
            )
        );
    }

    // Rechercher des opérations de crypto
    @PostMapping("/crypto")
    public ResponseEntity<?> rechercherOperationsCrypto(
        @RequestBody OperationFiltreRequestDTO filtres
    ) {
        // Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ValidationUser utilisateur = userService.findByUsername(username);

        // Rechercher les opérations de crypto
        Page<OperationFaitCrypto> operationsCrypto = 
            userOperationService.rechercherOperationsCryptoUtilisateur(utilisateur, filtres);

        return ResponseEntity.ok(
            Map.of(
                "operations", operationsCrypto.getContent().stream()
                    .map(op -> Map.of(
                        "id", op.getId(),
                        "typeCrypto", op.getTypeCrypto().getNom(),
                        "typeOperation", op.getOperationCrypto().getNom(),
                        "dateHeure", op.getDateHeure(),
                        "nombre", op.getNombre(),
                        "prixFinal", op.getPrixFinal(),
                        "commission", op.getCommission()
                    ))
                    .collect(Collectors.toList()),
                "totalPages", operationsCrypto.getTotalPages(),
                "totalElements", operationsCrypto.getTotalElements(),
                "pageActuelle", operationsCrypto.getNumber()
            )
        );
    }
}
