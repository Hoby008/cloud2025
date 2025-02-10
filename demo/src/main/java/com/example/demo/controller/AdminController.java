package com.example.demo.controller;

import com.example.demo.entity.ValidationUser;
import com.example.demo.repository.ValidationUserRepository;
import com.example.demo.security.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private ValidationUserRepository validationUserRepository;

    @Autowired
    private JwtTokenService jwtTokenService;

    // DTO pour masquer le mot de passe
    public static class UserDTO {
        private Long id;
        private String nom;
        private String prenom;
        private String email;
        private Integer roleId;
        private Integer numeroTentative;
        private Boolean etatValidation;
        private Boolean estTotalementInscrit;

        public UserDTO(ValidationUser user) {
            this.id = user.getId();
            this.nom = user.getNom();
            this.prenom = user.getPrenom();
            this.email = user.getEmail();
            this.roleId = user.getRoleId();
            this.numeroTentative = user.getNumeroTentative();
            this.etatValidation = user.getEtatValidation();
            this.estTotalementInscrit = user.getEstTotalementInscrit();
        }

        // Getters
        public Long getId() { return id; }
        public String getNom() { return nom; }
        public String getPrenom() { return prenom; }
        public String getEmail() { return email; }
        public Integer getRoleId() { return roleId; }
        public Integer getNumeroTentative() { return numeroTentative; }
        public Boolean getEtatValidation() { return etatValidation; }
        public Boolean getEstTotalementInscrit() { return estTotalementInscrit; }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestHeader("Authorization") String token) {
        // Extraire le token et vérifier le rôle
        String actualToken = token.substring(7);
        
        // Vérifier si le token est valide et si l'utilisateur est un admin
        if (!jwtTokenService.validateToken(actualToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token invalide");
        }

        String role = jwtTokenService.getRoleFromToken(actualToken);
        
        // Vérifier que l'utilisateur est un admin
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Accès réservé aux administrateurs");
        }

        // Récupérer tous les utilisateurs et les convertir en DTO
        List<ValidationUser> users = validationUserRepository.findAll();
        List<UserDTO> userDTOs = users.stream()
            .map(UserDTO::new)
            .collect(Collectors.toList());

        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserById(
        @PathVariable Long userId, 
        @RequestHeader("Authorization") String token
    ) {
        // Extraire le token et vérifier le rôle
        String actualToken = token.substring(7);
        
        // Vérifier si le token est valide et si l'utilisateur est un admin
        if (!jwtTokenService.validateToken(actualToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token invalide");
        }

        String role = jwtTokenService.getRoleFromToken(actualToken);
        
        // Vérifier que l'utilisateur est un admin
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Accès réservé aux administrateurs");
        }

        // Rechercher l'utilisateur
        ValidationUser user = validationUserRepository.findById(userId)
            .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Utilisateur non trouvé");
        }

        // Convertir en DTO pour masquer le mot de passe
        UserDTO userDTO = new UserDTO(user);

        return ResponseEntity.ok(userDTO);
    }
}
