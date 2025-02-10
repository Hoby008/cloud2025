package com.example.demo.controller;

import com.example.demo.entity.ValidationUser;
import com.example.demo.repository.ValidationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountUnlockController {

    @Autowired
    private ValidationUserRepository validationUserRepository;

    @GetMapping("/unlock")
    public ResponseEntity<?> unlockAccount(@RequestParam String token) {
        // Rechercher l'utilisateur avec ce token de déblocage
        List<ValidationUser> users = validationUserRepository.findByTokenDeblocage(token);
        
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Token de déblocage invalide");
        }

        ValidationUser user = users.get(0);

        // Vérifier si le token est encore valide
        if (user.getDateTokenDeblocage() == null || 
            LocalDateTime.now().isAfter(user.getDateTokenDeblocage())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Le token de déblocage a expiré");
        }

        // Débloquer le compte
        user.setEstBloque(false);
        user.setNumeroTentativeValidation(0);
        user.setTokenDeblocage(null);
        user.setDateTokenDeblocage(null);

        validationUserRepository.save(user);

        return ResponseEntity.ok("Votre compte a été débloqué avec succès. Vous pouvez maintenant vous connecter.");
    }
}
