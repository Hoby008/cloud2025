package com.example.demo.repository;

import com.example.demo.entity.Portefeuille;
import com.example.demo.entity.ValidationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortefeuilleRepository extends JpaRepository<Portefeuille, Long> {
    List<Portefeuille> findByUtilisateur(ValidationUser utilisateur);
    Optional<Portefeuille> findByUtilisateurId(Long utilisateurId);
}
