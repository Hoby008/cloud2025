package com.example.demo.controller;

import com.example.demo.dto.CoursCryptoAnalyseRequestDTO;
import com.example.demo.dto.CoursCryptoAnalyseResponseDTO;
import com.example.demo.service.CoursCryptoAnalyseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cours-cryptomonnaies/analyse")
public class CoursCryptoAnalyseController {

    @Autowired
    private CoursCryptoAnalyseService coursCryptoAnalyseService;

    @GetMapping("/types")
    public ResponseEntity<?> getTypesAnalyse() {
        Map<String, Object> response = new HashMap<>();
        
        // Types d'analyse disponibles
        response.put("typesAnalyse", new String[]{
            "1er quartile", "max", "min", "moyenne", "ecart-type"
        });
        
        // Cryptos disponibles
        response.put("cryptos", CoursCryptoAnalyseRequestDTO.getCryptosDisponibles());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/executer")
    public ResponseEntity<?> executerAnalyse(
        @RequestBody CoursCryptoAnalyseRequestDTO requete
    ) {
        try {
            CoursCryptoAnalyseResponseDTO resultat = 
                coursCryptoAnalyseService.analyserCoursCryptomonnaies(requete);
            
            return ResponseEntity.ok(resultat);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
            );
        }
    }
}
