package com.example.demo.repository;

import com.example.demo.entity.CoursCryptomonnaie;
import com.example.demo.entity.TypeCrypto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CoursCryptomonnaieRepository extends JpaRepository<CoursCryptomonnaie, Long> {
    // Rechercher les cours pour un type de crypto dans une période
    List<CoursCryptomonnaie> findByTypeCryptoAndDateHeureBetweenOrderByDateHeureAsc(
        TypeCrypto typeCrypto, 
        LocalDateTime dateDebut, 
        LocalDateTime dateFin
    );

    // Vérifier s'il existe des cours pour une période
    boolean existsByDateHeureBetween(
        LocalDateTime dateDebut, 
        LocalDateTime dateFin
    );
}
