package com.example.demo.repository;

import com.example.demo.entity.Commission;
import com.example.demo.entity.OperationCrypto;
import com.example.demo.entity.TypeCrypto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommissionRepository extends JpaRepository<Commission, Long> {
    // Méthode pour rechercher des commissions dans une plage de dates
    List<Commission> findByDateHeureCommissionBetween(
        LocalDateTime dateDebut, 
        LocalDateTime dateFin
    );

    // Méthode pour rechercher une commission par type d'opération et type de crypto
    Optional<Commission> findByOperationCryptoAndTypeCrypto(
        OperationCrypto operationCrypto, 
        TypeCrypto typeCrypto
    );
}
