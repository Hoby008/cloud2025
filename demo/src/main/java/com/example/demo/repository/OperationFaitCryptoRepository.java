package com.example.demo.repository;

import com.example.demo.entity.OperationFaitCrypto;
import com.example.demo.entity.TypeCrypto;
import com.example.demo.entity.ValidationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationFaitCryptoRepository extends JpaRepository<OperationFaitCrypto, Long> {
    // Trouver toutes les opérations d'un utilisateur
    List<OperationFaitCrypto> findByUtilisateur(ValidationUser utilisateur);

    // Trouver les opérations d'un utilisateur pour un type de crypto spécifique
    List<OperationFaitCrypto> findByUtilisateurAndTypeCrypto(
        ValidationUser utilisateur, 
        TypeCrypto typeCrypto
    );
}
