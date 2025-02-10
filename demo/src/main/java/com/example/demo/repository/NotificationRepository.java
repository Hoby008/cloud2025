package com.example.demo.repository;

import com.example.demo.entity.Notification;
import com.example.demo.entity.ValidationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Trouver les notifications non lues d'un utilisateur, triées par date de création décroissante
    List<Notification> findByUtilisateurAndLueFalseOrderByDateCreationDesc(ValidationUser utilisateur);

    // Supprimer les notifications lues d'un utilisateur
    void deleteByUtilisateurAndLueTrue(ValidationUser utilisateur);
}
