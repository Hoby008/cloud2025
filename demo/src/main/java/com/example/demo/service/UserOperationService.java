package com.example.demo.service;

import com.example.demo.dto.OperationFiltreRequestDTO;
import com.example.demo.entity.OperationFaitCrypto;
import com.example.demo.entity.OperationValidation;
import com.example.demo.entity.ValidationUser;
import com.example.demo.repository.OperationFaitCryptoRepository;
import com.example.demo.repository.OperationValidationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserOperationService {

    @Autowired
    private OperationValidationRepository operationValidationRepository;

    @Autowired
    private OperationFaitCryptoRepository operationFaitCryptoRepository;

    public Page<OperationValidation> rechercherOperationsValidationUtilisateur(
        ValidationUser utilisateur, 
        OperationFiltreRequestDTO filtres
    ) {
        Specification<OperationValidation> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtrer par l'utilisateur connecté
            predicates.add(criteriaBuilder.equal(root.get("utilisateur"), utilisateur));

            // Filtrer par types d'opération
            if (filtres.getTypesOperation() != null && !filtres.getTypesOperation().isEmpty()) {
                predicates.add(root.get("typeOperation").in(filtres.getTypesOperation()));
            }

            // Filtrer par date
            if (filtres.getDateMin() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateCreation"), filtres.getDateMin()));
            }
            if (filtres.getDateMax() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dateCreation"), filtres.getDateMax()));
            }

            // Filtrer par état de validation
            if (filtres.getEtatsValidation() != null && !filtres.getEtatsValidation().isEmpty()) {
                List<Predicate> etatPredicates = new ArrayList<>();
                
                if (filtres.getEtatsValidation().contains("En attente")) {
                    etatPredicates.add(criteriaBuilder.and(
                        criteriaBuilder.isFalse(root.get("estValide")),
                        criteriaBuilder.greaterThan(root.get("dateExpiration"), criteriaBuilder.currentTimestamp())
                    ));
                }
                
                if (filtres.getEtatsValidation().contains("Validé")) {
                    etatPredicates.add(criteriaBuilder.isTrue(root.get("estValide")));
                }
                
                if (filtres.getEtatsValidation().contains("Rejeté")) {
                    etatPredicates.add(criteriaBuilder.and(
                        criteriaBuilder.isFalse(root.get("estValide")),
                        criteriaBuilder.lessThan(root.get("dateExpiration"), criteriaBuilder.currentTimestamp())
                    ));
                }
                
                predicates.add(criteriaBuilder.or(
                    etatPredicates.toArray(new Predicate[0])
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        // Pagination
        Pageable pageable = PageRequest.of(filtres.getPage(), filtres.getTaillePage());
        
        return operationValidationRepository.findAll(spec, pageable);
    }

    public Page<OperationFaitCrypto> rechercherOperationsCryptoUtilisateur(
        ValidationUser utilisateur, 
        OperationFiltreRequestDTO filtres
    ) {
        Specification<OperationFaitCrypto> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtrer par l'utilisateur connecté
            predicates.add(criteriaBuilder.equal(root.get("utilisateur"), utilisateur));

            // Filtrer par types de crypto
            if (filtres.getTypesCrypto() != null && !filtres.getTypesCrypto().isEmpty()) {
                predicates.add(root.get("typeCrypto").get("nom").in(filtres.getTypesCrypto()));
            }

            // Filtrer par types d'opération
            if (filtres.getTypesOperation() != null && !filtres.getTypesOperation().isEmpty()) {
                predicates.add(root.get("operationCrypto").get("nom").in(filtres.getTypesOperation()));
            }

            // Filtrer par date
            if (filtres.getDateMin() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateHeure"), filtres.getDateMin()));
            }
            if (filtres.getDateMax() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dateHeure"), filtres.getDateMax()));
            }

            // Filtrer par montant
            if (filtres.getMontantMin() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("nombre").get("value"), filtres.getMontantMin()));
            }
            if (filtres.getMontantMax() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("nombre").get("value"), filtres.getMontantMax()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        // Pagination
        Pageable pageable = PageRequest.of(filtres.getPage(), filtres.getTaillePage());
        
        return operationFaitCryptoRepository.findAll(spec, pageable);
    }
}
