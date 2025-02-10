package com.example.demo.service;

import com.example.demo.dto.CommissionUpdateDTO;
import com.example.demo.entity.Commission;
import com.example.demo.entity.OperationCrypto;
import com.example.demo.entity.TypeCrypto;
import com.example.demo.repository.CommissionRepository;
import com.example.demo.repository.OperationCryptoRepository;
import com.example.demo.repository.TypeCryptoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminCommissionService {

    @Autowired
    private CommissionRepository commissionRepository;

    @Autowired
    private TypeCryptoRepository typeCryptoRepository;

    @Autowired
    private OperationCryptoRepository operationCryptoRepository;

    // Récupérer les commissions actuelles
    public List<CommissionUpdateDTO> getCurrentCommissions() {
        List<Commission> commissions = commissionRepository.findAll();
        
        return commissions.stream()
            .map(commission -> new CommissionUpdateDTO(
                commission.getId(),
                commission.getTypeCrypto().getNom(),
                commission.getOperationCrypto().getLibelle(),
                commission.getPourcentage()
            ))
            .collect(Collectors.toList());
    }

    // Mettre à jour les commissions
    @Transactional
    public List<CommissionUpdateDTO> updateCommissions(List<CommissionUpdateDTO> commissionUpdates) {
        // Valider les mises à jour
        List<CommissionUpdateDTO> validUpdates = commissionUpdates.stream()
            .filter(CommissionUpdateDTO::isValid)
            .collect(Collectors.toList());

        if (validUpdates.isEmpty()) {
            throw new IllegalArgumentException("Aucune mise à jour valide fournie");
        }

        List<CommissionUpdateDTO> updatedCommissions = new ArrayList<>();

        for (CommissionUpdateDTO update : validUpdates) {
            // Trouver la commission correspondante
            Optional<Commission> commissionOpt = commissionRepository.findById(update.getId());
            
            if (commissionOpt.isPresent()) {
                Commission commission = commissionOpt.get();
                
                // Mettre à jour le pourcentage
                commission.setPourcentage(
                    update.getPourcentage().setScale(2, RoundingMode.HALF_UP)
                );
                
                // Sauvegarder la commission mise à jour
                Commission updatedCommission = commissionRepository.save(commission);
                
                // Convertir et ajouter à la liste des mises à jour
                updatedCommissions.add(new CommissionUpdateDTO(
                    updatedCommission.getId(),
                    updatedCommission.getTypeCrypto().getNom(),
                    updatedCommission.getOperationCrypto().getLibelle(),
                    updatedCommission.getPourcentage()
                ));
            }
        }

        return updatedCommissions;
    }

    // Réinitialiser les commissions aux valeurs par défaut
    @Transactional
    public List<CommissionUpdateDTO> resetCommissionsToDefault() {
        // Supprimer toutes les commissions existantes
        commissionRepository.deleteAll();

        // Récupérer les types de cryptos et les opérations
        List<TypeCrypto> typeCryptos = typeCryptoRepository.findAll();
        OperationCrypto achat = operationCryptoRepository.findByLibelle("Achat")
            .orElseThrow(() -> new RuntimeException("Opération Achat non trouvée"));
        OperationCrypto vente = operationCryptoRepository.findByLibelle("Vente")
            .orElseThrow(() -> new RuntimeException("Opération Vente non trouvée"));

        List<Commission> defaultCommissions = new ArrayList<>();
        List<CommissionUpdateDTO> defaultCommissionDTOs = new ArrayList<>();

        // Générer des commissions par défaut
        for (TypeCrypto typeCrypto : typeCryptos) {
            // Commission d'achat par défaut
            Commission commissionAchat = new Commission(
                BigDecimal.valueOf(0.5), // Valeur par défaut pour achat
                achat,
                typeCrypto,
                LocalDateTime.now()
            );
            defaultCommissions.add(commissionAchat);

            // Commission de vente par défaut
            Commission commissionVente = new Commission(
                BigDecimal.valueOf(1.0), // Valeur par défaut pour vente
                vente,
                typeCrypto,
                LocalDateTime.now()
            );
            defaultCommissions.add(commissionVente);

            // Convertir en DTO
            defaultCommissionDTOs.add(new CommissionUpdateDTO(
                null, 
                typeCrypto.getNom(), 
                "Achat", 
                BigDecimal.valueOf(0.5)
            ));
            defaultCommissionDTOs.add(new CommissionUpdateDTO(
                null, 
                typeCrypto.getNom(), 
                "Vente", 
                BigDecimal.valueOf(1.0)
            ));
        }

        // Sauvegarder les commissions par défaut
        commissionRepository.saveAll(defaultCommissions);

        return defaultCommissionDTOs;
    }
}
