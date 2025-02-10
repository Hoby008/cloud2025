package com.example.demo.config;

import com.example.demo.entity.TypeCrypto;
import com.example.demo.repository.TypeCryptoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CryptoInitializationConfig {

    @Autowired
    private TypeCryptoRepository typeCryptoRepository;

    @Bean
    public CommandLineRunner initializeCryptoTypes() {
        return args -> {
            // Liste des cryptomonnaies à initialiser
            List<TypeCrypto> cryptoTypes = Arrays.asList(
                new TypeCrypto("Bitcoin", "BTC"),
                new TypeCrypto("Ethereum", "ETH"),
                new TypeCrypto("Litecoin", "LTC"),
                new TypeCrypto("Cardano", "ADA"),
                new TypeCrypto("Solana", "SOL"),
                new TypeCrypto("Ripple", "XRP"),
                new TypeCrypto("Polkadot", "DOT"),
                new TypeCrypto("Dogecoin", "DOGE"),
                new TypeCrypto("Chainlink", "LINK"),
                new TypeCrypto("Stellar", "XLM")
            );

            // Vérifier si les types de cryptos existent déjà
            List<TypeCrypto> existingTypes = typeCryptoRepository.findAll();
            
            if (existingTypes.isEmpty()) {
                // Sauvegarder tous les types de cryptos
                typeCryptoRepository.saveAll(cryptoTypes);
                System.out.println("Types de cryptomonnaies initialisés avec succès.");
            } else {
                System.out.println("Types de cryptomonnaies déjà existants.");
            }
        };
    }
}
