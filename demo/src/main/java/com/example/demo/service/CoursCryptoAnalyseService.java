package com.example.demo.service;

import com.example.demo.dto.CoursCryptoAnalyseRequestDTO;
import com.example.demo.dto.CoursCryptoAnalyseResponseDTO;
import com.example.demo.entity.CoursCryptomonnaie;
import com.example.demo.entity.TypeCrypto;
import com.example.demo.repository.CoursCryptomonnaieRepository;
import com.example.demo.repository.TypeCryptoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CoursCryptoAnalyseService {

    @Autowired
    private CoursCryptomonnaieRepository coursCryptomonnaieRepository;

    @Autowired
    private TypeCryptoRepository typeCryptoRepository;

    public CoursCryptoAnalyseResponseDTO analyserCoursCryptomonnaies(CoursCryptoAnalyseRequestDTO requete) {
        // Valider la requête
        if (!requete.isValid()) {
            throw new IllegalArgumentException("Paramètres d'analyse invalides");
        }

        // Récupérer les cours filtrés
        List<CoursCryptomonnaie> coursFiletees = filtrerCours(requete);

        // Calculer le résultat selon le type d'analyse
        BigDecimal resultat;
        List<Map<String, Object>> details = new ArrayList<>();

        switch (requete.getTypeAnalyse()) {
            case "1er quartile":
                resultat = calculerPremierQuartile(coursFiletees);
                break;
            case "max":
                resultat = calculerMax(coursFiletees);
                break;
            case "min":
                resultat = calculerMin(coursFiletees);
                break;
            case "moyenne":
                resultat = calculerMoyenne(coursFiletees);
                break;
            case "ecart-type":
                resultat = calculerEcartType(coursFiletees);
                break;
            default:
                throw new IllegalArgumentException("Type d'analyse non reconnu");
        }

        // Préparer les détails
        preparerDetailsAnalyse(coursFiletees, details);

        return new CoursCryptoAnalyseResponseDTO(
            requete.getTypeAnalyse(),
            requete.getCrypto(),
            resultat.setScale(4, RoundingMode.HALF_UP),
            details
        );
    }

    private List<CoursCryptomonnaie> filtrerCours(CoursCryptoAnalyseRequestDTO requete) {
        // Récupérer toutes les commissions dans la plage de dates
        List<CoursCryptomonnaie> cours;

        // Filtrer par crypto si nécessaire
        if (!"Tous".equals(requete.getCrypto())) {
            TypeCrypto typeCrypto = typeCryptoRepository.findByNom(requete.getCrypto())
                .orElseThrow(() -> new IllegalArgumentException("Crypto non trouvée"));
            
            cours = coursCryptomonnaieRepository.findByTypeCryptoAndDateHeureBetweenOrderByDateHeureAsc(
                typeCrypto, 
                requete.getDateMin(), 
                requete.getDateMax()
            );
        } else {
            // Récupérer tous les cours de toutes les cryptos
            cours = coursCryptomonnaieRepository.findAll().stream()
                .filter(c -> 
                    c.getDateHeure().isAfter(requete.getDateMin()) && 
                    c.getDateHeure().isBefore(requete.getDateMax())
                )
                .collect(Collectors.toList());
        }

        return cours;
    }

    // Calcul du premier quartile
    private BigDecimal calculerPremierQuartile(List<CoursCryptomonnaie> cours) {
        if (cours.isEmpty()) return BigDecimal.ZERO;

        List<BigDecimal> prix = cours.stream()
            .map(CoursCryptomonnaie::getPrix)
            .sorted()
            .collect(Collectors.toList());

        int index = (int) Math.ceil(prix.size() * 0.25) - 1;
        return prix.get(Math.max(0, index));
    }

    // Calcul du maximum
    private BigDecimal calculerMax(List<CoursCryptomonnaie> cours) {
        return cours.stream()
            .map(CoursCryptomonnaie::getPrix)
            .max(BigDecimal::compareTo)
            .orElse(BigDecimal.ZERO);
    }

    // Calcul du minimum
    private BigDecimal calculerMin(List<CoursCryptomonnaie> cours) {
        return cours.stream()
            .map(CoursCryptomonnaie::getPrix)
            .min(BigDecimal::compareTo)
            .orElse(BigDecimal.ZERO);
    }

    // Calcul de la moyenne
    private BigDecimal calculerMoyenne(List<CoursCryptomonnaie> cours) {
        if (cours.isEmpty()) return BigDecimal.ZERO;

        BigDecimal somme = cours.stream()
            .map(CoursCryptomonnaie::getPrix)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return somme.divide(BigDecimal.valueOf(cours.size()), 8, RoundingMode.HALF_UP);
    }

    // Calcul de l'écart-type
    private BigDecimal calculerEcartType(List<CoursCryptomonnaie> cours) {
        if (cours.isEmpty()) return BigDecimal.ZERO;

        BigDecimal moyenne = calculerMoyenne(cours);

        BigDecimal sommeDiffCarres = cours.stream()
            .map(c -> {
                BigDecimal diff = c.getPrix().subtract(moyenne);
                return diff.multiply(diff);
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal variance = sommeDiffCarres.divide(
            BigDecimal.valueOf(cours.size()), 
            8, 
            RoundingMode.HALF_UP
        );

        return BigDecimal.valueOf(Math.sqrt(variance.doubleValue()));
    }

    // Préparer les détails de l'analyse
    private void preparerDetailsAnalyse(
        List<CoursCryptomonnaie> cours, 
        List<Map<String, Object>> details
    ) {
        // Regrouper les cours par crypto
        Map<String, List<CoursCryptomonnaie>> groupedCours = cours.stream()
            .collect(Collectors.groupingBy(c -> c.getTypeCrypto().getNom()));

        // Convertir en liste de détails
        groupedCours.forEach((crypto, coursCrypto) -> {
            Map<String, Object> detail = new HashMap<>();
            detail.put("crypto", crypto);
            detail.put("nombreCours", coursCrypto.size());
            detail.put("min", calculerMin(coursCrypto));
            detail.put("max", calculerMax(coursCrypto));
            detail.put("moyenne", calculerMoyenne(coursCrypto));
            detail.put("ecartType", calculerEcartType(coursCrypto));
            details.add(detail);
        });
    }
}
