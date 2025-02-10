package com.example.demo.service;

import com.example.demo.entity.Commission;
import com.example.demo.entity.OperationCrypto;
import com.example.demo.entity.TypeCrypto;
import com.example.demo.repository.CommissionRepository;
import com.example.demo.repository.OperationCryptoRepository;
import com.example.demo.repository.TypeCryptoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class CommissionService {

    @Autowired
    private CommissionRepository commissionRepository;

    @Autowired
    private TypeCryptoRepository typeCryptoRepository;

    @Autowired
    private OperationCryptoRepository operationCryptoRepository;

    private final Random random = new Random();

    // Méthode pour initialiser les opérations crypto si elles n'existent pas
    private void initializeOperationsCrypto() {
        if (operationCryptoRepository.findByLibelle("Achat").isEmpty()) {
            OperationCrypto achat = new OperationCrypto("Achat");
            operationCryptoRepository.save(achat);
        }
        if (operationCryptoRepository.findByLibelle("Vente").isEmpty()) {
            OperationCrypto vente = new OperationCrypto("Vente");
            operationCryptoRepository.save(vente);
        }
    }

    // Générer un pourcentage de commission aléatoire
    private BigDecimal genererPourcentageCommission(boolean isAchat) {
        // Commissions d'achat entre 0.3% et 1.2%
        // Commissions de vente entre 0.8% et 1.8%
        double min = isAchat ? 0.3 : 0.8;
        double max = isAchat ? 1.2 : 1.8;
        
        return BigDecimal.valueOf(min + (max - min) * random.nextDouble())
            .setScale(2, RoundingMode.HALF_UP);
    }

    @Transactional
    @Scheduled(fixedRate = 10000) // Toutes les 10 secondes
    public void genererCommissionsAutomatiques() {
        // Initialiser les opérations crypto si nécessaire
        initializeOperationsCrypto();

        // Récupérer les types de cryptos
        List<TypeCrypto> typeCryptos = typeCryptoRepository.findAll();

        // Récupérer les opérations crypto
        OperationCrypto achat = operationCryptoRepository.findByLibelle("Achat")
            .orElseThrow(() -> new RuntimeException("Opération Achat non trouvée"));
        OperationCrypto vente = operationCryptoRepository.findByLibelle("Vente")
            .orElseThrow(() -> new RuntimeException("Opération Vente non trouvée"));

        // Date actuelle et dates des 3 derniers jours
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime[] dates = {
            now,
            now.minusDays(1),
            now.minusDays(2),
            now.minusDays(3)
        };

        // Générer des commissions pour chaque crypto et chaque date
        for (TypeCrypto typeCrypto : typeCryptos) {
            for (LocalDateTime date : dates) {
                // Générer une commission d'achat
                Commission commissionAchat = new Commission(
                    genererPourcentageCommission(true),
                    achat,
                    typeCrypto,
                    date
                );
                commissionRepository.save(commissionAchat);

                // Générer une commission de vente
                Commission commissionVente = new Commission(
                    genererPourcentageCommission(false),
                    vente,
                    typeCrypto,
                    date
                );
                commissionRepository.save(commissionVente);
            }
        }

        System.out.println("Génération automatique des commissions terminée.");
    }
}
