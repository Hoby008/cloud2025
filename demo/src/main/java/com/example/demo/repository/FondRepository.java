package com.example.demo.repository;

import com.example.demo.entity.Fond;
import com.example.demo.entity.Portefeuille;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FondRepository extends JpaRepository<Fond, Long> {
    Optional<Fond> findByPortefeuilleId(Long portefeuilleId);
    Optional<Fond> findByPortefeuille(Portefeuille portefeuille);
}
