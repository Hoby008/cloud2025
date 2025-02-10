package com.example.demo.config;

import com.example.demo.service.CoursCryptomonnaieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoursCryptoInitializationConfig {

    @Autowired
    private CoursCryptomonnaieService coursCryptomonnaieService;

    @Bean
    public CommandLineRunner initializeCoursCryptomonnaies() {
        return args -> {
            // Initialiser les cours des cryptomonnaies au démarrage de l'application
            coursCryptomonnaieService.initialiserCoursCryptomonnaies();
        };
    }
}
