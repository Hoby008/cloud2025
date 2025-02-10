package com.example.demo.controller;

import com.example.demo.entity.CoursCryptomonnaie;
import com.example.demo.entity.TypeCrypto;
import com.example.demo.repository.CoursCryptomonnaieRepository;
import com.example.demo.repository.TypeCryptoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cours-cryptomonnaies")
public class CoursCryptomonnaieController {

    @Autowired
    private CoursCryptomonnaieRepository coursCryptomonnaieRepository;

    @Autowired
    private TypeCryptoRepository typeCryptoRepository;

    @GetMapping("/liste")
    public ResponseEntity<?> getCoursCryptomonnaies(
        @RequestParam(value = "crypto", required = false) String nomCrypto,
        @RequestParam(value = "dateDebut", required = false) String dateDebutStr,
        @RequestParam(value = "dateFin", required = false) String dateFinStr
    ) {
        // Définir les dates par défaut (3 derniers jours)
        LocalDateTime dateFin = LocalDateTime.now();
        LocalDateTime dateDebut = dateFin.minusDays(3);

        // Filtrer par crypto si spécifié
        List<TypeCrypto> typesCrypto;
        if (nomCrypto != null && !nomCrypto.isEmpty()) {
            TypeCrypto typeCrypto = typeCryptoRepository.findByNom(nomCrypto)
                .orElseThrow(() -> new IllegalArgumentException("Crypto non trouvée"));
            typesCrypto = List.of(typeCrypto);
        } else {
            typesCrypto = typeCryptoRepository.findAll();
        }

        // Récupérer les cours
        List<Map<String, Object>> coursCryptos = typesCrypto.stream()
            .map(typeCrypto -> {
                List<CoursCryptomonnaie> cours = coursCryptomonnaieRepository
                    .findByTypeCryptoAndDateHeureBetweenOrderByDateHeureAsc(
                        typeCrypto, dateDebut, dateFin
                    );

                return Map.of(
                    "crypto", typeCrypto.getNom(),
                    "symbole", typeCrypto.getSymbole(),
                    "cours", cours.stream()
                        .map(c -> Map.of(
                            "prix", c.getPrix(),
                            "dateHeure", c.getDateHeure()
                        ))
                        .collect(Collectors.toList())
                );
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok(coursCryptos);
    }

    @GetMapping("/cryptos")
    public ResponseEntity<?> getCryptosDisponibles() {
        List<TypeCrypto> typesCrypto = typeCryptoRepository.findAll();
        
        List<Map<String, String>> cryptos = typesCrypto.stream()
            .map(typeCrypto -> Map.of(
                "nom", typeCrypto.getNom(),
                "symbole", typeCrypto.getSymbole()
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(cryptos);
    }
}
