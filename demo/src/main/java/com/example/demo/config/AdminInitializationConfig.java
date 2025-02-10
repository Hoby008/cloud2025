package com.example.demo.config;

import com.example.demo.entity.ValidationUser;
import com.example.demo.repository.ValidationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Configuration
public class AdminInitializationConfig {

    @Autowired
    private ValidationUserRepository validationUserRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initializeAdminUsers() {
        return args -> {
            // Vérifier si des comptes admin existent déjà
            List<ValidationUser> adminUsers = validationUserRepository.findByRoleId(1);
            
            if (adminUsers.isEmpty()) {
                // Créer des comptes admin par défaut
                ValidationUser admin1 = new ValidationUser();
                admin1.setNom("Admin");
                admin1.setPrenom("Principal");
                admin1.setEmail("admin.principal@company.com");
                admin1.setPassword(passwordEncoder.encode("AdminPass2025!"));
                admin1.setRoleId(1); // Role admin
                admin1.setEtatValidation(true);
                admin1.setEstTotalementInscrit(true);
                admin1.setNumeroTentative(1);

                ValidationUser admin2 = new ValidationUser();
                admin2.setNom("Admin");
                admin2.setPrenom("Secondaire");
                admin2.setEmail("admin.secondaire@company.com");
                admin2.setPassword(passwordEncoder.encode("AdminPass2025@"));
                admin2.setRoleId(1); // Role admin
                admin2.setEtatValidation(true);
                admin2.setEstTotalementInscrit(true);
                admin2.setNumeroTentative(1);

                validationUserRepository.saveAll(List.of(admin1, admin2));
                
                System.out.println("Comptes admin initialisés avec succès.");
            }
        };
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
