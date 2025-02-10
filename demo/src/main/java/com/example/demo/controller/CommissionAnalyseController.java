package com.example.demo.controller;

import com.example.demo.dto.CommissionAnalyseRequestDTO;
import com.example.demo.dto.CommissionAnalyseResponseDTO;
import com.example.demo.service.CommissionAnalyseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/commissions/analyse")
public class CommissionAnalyseController {

    @Autowired
    private CommissionAnalyseService commissionAnalyseService;

    @GetMapping("/types")
    public ResponseEntity<?> getTypesAnalyse() {
        Map<String, Object> response = new HashMap<>();
        
        // Types d'analyse disponibles
        response.put("typesAnalyse", new String[]{"Somme", "Moyenne"});
        
        // Cryptos disponibles
        response.put("cryptos", CommissionAnalyseRequestDTO.getCryptosDisponibles());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/executer")
    public ResponseEntity<?> executerAnalyse(
        @RequestBody CommissionAnalyseRequestDTO requete
    ) {
        try {
            CommissionAnalyseResponseDTO resultat = 
                commissionAnalyseService.analyserCommissions(requete);
            
            return ResponseEntity.ok(resultat);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
            );
        }
    }
}
