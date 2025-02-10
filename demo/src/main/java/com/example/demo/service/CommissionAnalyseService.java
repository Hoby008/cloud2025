package com.example.demo.service;

import com.example.demo.dto.CommissionAnalyseRequestDTO;
import com.example.demo.dto.CommissionAnalyseResponseDTO;
import com.example.demo.entity.Commission;
import com.example.demo.entity.TypeCrypto;
import com.example.demo.repository.CommissionRepository;
import com.example.demo.repository.TypeCryptoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommissionAnalyseService {

    @Autowired
    private CommissionRepository commissionRepository;

    @Autowired
    private TypeCryptoRepository typeCryptoRepository;

    public CommissionAnalyseResponseDTO analyserCommissions(CommissionAnalyseRequestDTO requete) {
        // Valider la requête
        if (!requete.isValid()) {
            throw new IllegalArgumentException("Paramètres d'analyse invalides");
        }

        // Récupérer les commissions dans la plage de dates
        List<Commission> commissionsFiletees = filtrerCommissions(requete);

        // Calculer le résultat selon le type d'analyse
        BigDecimal resultat;
        List<Map<String, Object>> details = new ArrayList<>();

        if ("Somme".equals(requete.getTypeAnalyse())) {
            resultat = calculerSommeCommissions(commissionsFiletees);
        } else { // Moyenne
            resultat = calculerMoyenneCommissions(commissionsFiletees);
        }

        // Préparer les détails
        preparerDetailsCommissions(commissionsFiletees, details);

        return new CommissionAnalyseResponseDTO(
            requete.getTypeAnalyse(),
            requete.getCrypto(),
            resultat.setScale(2, RoundingMode.HALF_UP),
            details
        );
    }

    private List<Commission> filtrerCommissions(CommissionAnalyseRequestDTO requete) {
        // Récupérer toutes les commissions dans la plage de dates
        List<Commission> commissions = commissionRepository.findByDateHeureCommissionBetween(
            requete.getDateMin(), 
            requete.getDateMax()
        );

        // Filtrer par crypto si nécessaire
        if (!"Tous".equals(requete.getCrypto())) {
            TypeCrypto typeCrypto = typeCryptoRepository.findByNom(requete.getCrypto())
                .orElseThrow(() -> new IllegalArgumentException("Crypto non trouvée"));
            
            commissions = commissions.stream()
                .filter(c -> c.getTypeCrypto().getId().equals(typeCrypto.getId()))
                .collect(Collectors.toList());
        }

        return commissions;
    }

    private BigDecimal calculerSommeCommissions(List<Commission> commissions) {
        return commissions.stream()
            .map(Commission::getPourcentage)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculerMoyenneCommissions(List<Commission> commissions) {
        if (commissions.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal somme = calculerSommeCommissions(commissions);
        return somme.divide(BigDecimal.valueOf(commissions.size()), 4, RoundingMode.HALF_UP);
    }

    private void preparerDetailsCommissions(
        List<Commission> commissions, 
        List<Map<String, Object>> details
    ) {
        // Regrouper les commissions par crypto et type d'opération
        Map<String, List<Commission>> groupedCommissions = commissions.stream()
            .collect(Collectors.groupingBy(c -> 
                c.getTypeCrypto().getNom() + " - " + c.getOperationCrypto().getLibelle()
            ));

        // Convertir en liste de détails
        groupedCommissions.forEach((key, commissionsGroupe) -> {
            Map<String, Object> detail = new HashMap<>();
            detail.put("groupe", key);
            detail.put("nombreCommissions", commissionsGroupe.size());
            detail.put("sommePourcentages", calculerSommeCommissions(commissionsGroupe));
            detail.put("moyennePourcentages", calculerMoyenneCommissions(commissionsGroupe));
            details.add(detail);
        });
    }
}
