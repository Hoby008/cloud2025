package com.example.demo.repository;

import com.example.demo.entity.OperationValidation;
import com.example.demo.entity.ValidationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OperationValidationRepository extends JpaRepository<OperationValidation, Long> {
    // Trouver une validation par son token
    Optional<OperationValidation> findByToken(String token);

    // Trouver les validations non expirées pour un utilisateur
    List<OperationValidation> findByUtilisateurAndEstValideAndDateExpirationAfter(
        ValidationUser utilisateur, 
        Boolean estValide, 
        LocalDateTime dateActuelle
    );

    // Supprimer les validations expirées
    void deleteByDateExpirationBefore(LocalDateTime dateActuelle);
}
