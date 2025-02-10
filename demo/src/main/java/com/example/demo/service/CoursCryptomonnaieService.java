package com.example.demo.service;

import com.example.demo.entity.CoursCryptomonnaie;
import com.example.demo.entity.TypeCrypto;
import com.example.demo.repository.CoursCryptomonnaieRepository;
import com.example.demo.repository.TypeCryptoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class CoursCryptomonnaieService {

    @Autowired
    private CoursCryptomonnaieRepository coursCryptomonnaieRepository;

    @Autowired
    private TypeCryptoRepository typeCryptoRepository;

    private final Random random = new Random();

    // Prix initiaux pour chaque cryptomonnaie
    private static final BigDecimal[] PRIX_INITIAUX = {
        BigDecimal.valueOf(43250.75),  // Bitcoin
        BigDecimal.valueOf(3120.50),   // Ethereum
        BigDecimal.valueOf(152.75),    // Litecoin
        BigDecimal.valueOf(0.58),      // Cardano
        BigDecimal.valueOf(102.30),    // Solana
        BigDecimal.valueOf(0.50),      // Ripple
        BigDecimal.valueOf(7.50),      // Polkadot
        BigDecimal.valueOf(0.20),      // Dogecoin
        BigDecimal.valueOf(15.00),     // Chainlink
        BigDecimal.valueOf(0.15)       // Stellar
    };

    // Générer un cours de cryptomonnaie
    private BigDecimal genererPrixCrypto(BigDecimal prixActuel) {
        // Variation entre -5% et +5%
        double variation = (random.nextDouble() * 0.1) - 0.05;
        return prixActuel.multiply(BigDecimal.valueOf(1 + variation))
            .setScale(8, RoundingMode.HALF_UP);
    }

    // Initialiser les cours pour les 3 derniers jours
    @Transactional
    public void initialiserCoursCryptomonnaies() {
        // Vérifier si des cours existent déjà
        LocalDateTime now = LocalDateTime.now();
        if (coursCryptomonnaieRepository.existsByDateHeureBetween(
            now.minusDays(3), now
        )) {
            return;
        }

        // Récupérer tous les types de cryptos
        List<TypeCrypto> typeCryptos = typeCryptoRepository.findAll();

        // Liste pour stocker tous les cours
        List<CoursCryptomonnaie> coursCryptomonnaies = new ArrayList<>();

        // Générer des cours pour les 3 derniers jours
        for (int jour = 3; jour >= 0; jour--) {
            for (int i = 0; i < typeCryptos.size(); i++) {
                TypeCrypto typeCrypto = typeCryptos.get(i);
                BigDecimal prixInitial = PRIX_INITIAUX[i];

                // Générer des cours à différentes heures
                for (int heure : new int[]{0, 6, 12, 18}) {
                    LocalDateTime dateHeure = now.minusDays(jour).withHour(heure).withMinute(0).withSecond(0);
                    
                    // Calculer le prix
                    BigDecimal prix = (jour == 3) ? prixInitial : 
                        genererPrixCrypto(coursCryptomonnaies.get(coursCryptomonnaies.size() - 1).getPrix());

                    CoursCryptomonnaie coursCrypto = new CoursCryptomonnaie(
                        typeCrypto, prix, dateHeure
                    );
                    coursCryptomonnaies.add(coursCrypto);
                }
            }
        }

        // Sauvegarder tous les cours
        coursCryptomonnaieRepository.saveAll(coursCryptomonnaies);
    }

    // Générer des cours toutes les heures
    @Transactional
    @Scheduled(fixedRate = 3600000) // Toutes les heures
    public void genererCoursCryptomonnaiesAutomatiquement() {
        // Récupérer tous les types de cryptos
        List<TypeCrypto> typeCryptos = typeCryptoRepository.findAll();

        // Liste pour stocker les nouveaux cours
        List<CoursCryptomonnaie> nouveauxCours = new ArrayList<>();

        // Générer un cours pour chaque crypto
        LocalDateTime now = LocalDateTime.now();
        for (TypeCrypto typeCrypto : typeCryptos) {
            // Trouver le dernier cours de cette crypto
            List<CoursCryptomonnaie> coursPrecedents = 
                coursCryptomonnaieRepository.findByTypeCryptoAndDateHeureBetweenOrderByDateHeureAsc(
                    typeCrypto, 
                    now.minusDays(1), 
                    now
                );

            BigDecimal dernierPrix = coursPrecedents.isEmpty() ? 
                PRIX_INITIAUX[typeCryptos.indexOf(typeCrypto)] : 
                coursPrecedents.get(coursPrecedents.size() - 1).getPrix();

            // Générer un nouveau cours
            CoursCryptomonnaie nouveauCours = new CoursCryptomonnaie(
                typeCrypto,
                genererPrixCrypto(dernierPrix),
                now
            );

            nouveauxCours.add(nouveauCours);
        }

        // Sauvegarder les nouveaux cours
        coursCryptomonnaieRepository.saveAll(nouveauxCours);
    }
}
