package com.example.demo.controller;

import com.example.demo.dto.OperationFiltreRequestDTO;
import com.example.demo.entity.OperationFaitCrypto;
import com.example.demo.entity.OperationValidation;
import com.example.demo.service.AdminOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/operations")
@PreAuthorize("hasRole('ADMIN')")
public class AdminOperationController {

    @Autowired
    private AdminOperationService adminOperationService;

    // Récupérer les filtres disponibles
    @GetMapping("/filtres")
    public ResponseEntity<?> getFiltresDisponibles() {
        Map<String, Object> filtres = new HashMap<>();
        
        filtres.put("typesOperation", OperationFiltreRequestDTO.getTypesOperationPossibles());
        filtres.put("typesCrypto", OperationFiltreRequestDTO.getTypesCryptoPossibles());
        filtres.put("etatsValidation", OperationFiltreRequestDTO.getEtatsValidationPossibles());
        
        return ResponseEntity.ok(filtres);
    }

    // Rechercher des opérations de validation
    @PostMapping("/validations")
    public ResponseEntity<?> rechercherOperationsValidation(
        @RequestBody OperationFiltreRequestDTO filtres
    ) {
        Page<OperationValidation> operationsValidation = 
            adminOperationService.rechercherOperationsValidation(filtres);

        return ResponseEntity.ok(
            Map.of(
                "operations", operationsValidation.getContent().stream()
                    .map(op -> Map.of(
                        "id", op.getId(),
                        "typeOperation", op.getTypeOperation(),
                        "dateCreation", op.getDateCreation(),
                        "dateExpiration", op.getDateExpiration(),
                        "estValide", op.getEstValide(),
                        "utilisateur", op.getUtilisateur().getUsername(),
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
        Page<OperationFaitCrypto> operationsCrypto = 
            adminOperationService.rechercherOperationsCrypto(filtres);

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
                        "commission", op.getCommission(),
                        "utilisateur", op.getUtilisateur().getUsername()
                    ))
                    .collect(Collectors.toList()),
                "totalPages", operationsCrypto.getTotalPages(),
                "totalElements", operationsCrypto.getTotalElements(),
                "pageActuelle", operationsCrypto.getNumber()
            )
        );
    }
}
