package com.exemple.Cloud.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exemple.Cloud.Model.Portefeuille;
import com.exemple.Cloud.Model.User;

import java.util.Optional;

@Repository
public interface PortefeuilleRepository extends JpaRepository<Portefeuille, Long> {
    Optional<Portefeuille> findByUser(User user);
}