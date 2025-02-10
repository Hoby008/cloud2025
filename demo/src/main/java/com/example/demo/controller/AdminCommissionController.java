package com.example.demo.controller;

import com.example.demo.dto.CommissionUpdateDTO;
import com.example.demo.service.AdminCommissionService;
import com.example.demo.security.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/commissions")
public class AdminCommissionController {

    @Autowired
    private AdminCommissionService adminCommissionService;

    @Autowired
    private JwtTokenService jwtTokenService;

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentCommissions(@RequestHeader("Authorization") String token) {
        // Vérifier que l'utilisateur est un admin
        String actualToken = token.substring(7);
        if (!jwtTokenService.validateToken(actualToken) || 
            !jwtTokenService.getRoleFromToken(actualToken).equals("ROLE_ADMIN")) {
            return ResponseEntity.status(403).body("Accès non autorisé");
        }

        return ResponseEntity.ok(adminCommissionService.getCurrentCommissions());
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCommissions(
        @RequestHeader("Authorization") String token,
        @RequestBody List<CommissionUpdateDTO> commissionUpdates
    ) {
        // Vérifier que l'utilisateur est un admin
        String actualToken = token.substring(7);
        if (!jwtTokenService.validateToken(actualToken) || 
            !jwtTokenService.getRoleFromToken(actualToken).equals("ROLE_ADMIN")) {
            return ResponseEntity.status(403).body("Accès non autorisé");
        }

        try {
            List<CommissionUpdateDTO> updatedCommissions = 
                adminCommissionService.updateCommissions(commissionUpdates);
            
            return ResponseEntity.ok(Map.of(
                "message", "Commissions mises à jour avec succès",
                "commissions", updatedCommissions
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", e.getMessage()
            ));
        }
    }

    @PostMapping("/reset-to-default")
    public ResponseEntity<?> resetCommissionsToDefault(
        @RequestHeader("Authorization") String token
    ) {
        // Vérifier que l'utilisateur est un admin
        String actualToken = token.substring(7);
        if (!jwtTokenService.validateToken(actualToken) || 
            !jwtTokenService.getRoleFromToken(actualToken).equals("ROLE_ADMIN")) {
            return ResponseEntity.status(403).body("Accès non autorisé");
        }

        try {
            List<CommissionUpdateDTO> defaultCommissions = 
                adminCommissionService.resetCommissionsToDefault();
            
            return ResponseEntity.ok(Map.of(
                "message", "Commissions réinitialisées aux valeurs par défaut",
                "commissions", defaultCommissions
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Erreur lors de la réinitialisation des commissions"
            ));
        }
    }
}
