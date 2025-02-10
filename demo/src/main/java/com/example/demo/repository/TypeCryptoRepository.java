package com.example.demo.repository;

import com.example.demo.entity.TypeCrypto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeCryptoRepository extends JpaRepository<TypeCrypto, Long> {
    Optional<TypeCrypto> findByNom(String nom);
}
