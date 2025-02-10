package com.example.demo.controller;

import com.example.demo.entity.ValidationUser;
import com.example.demo.service.PhotoProfilService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/user/photo-profil")
public class PhotoProfilController {

    @Autowired
    private PhotoProfilService photoProfilService;

    @Autowired
    private UserService userService;

    // Télécharger une photo de profil
    @PostMapping("/upload")
    public ResponseEntity<?> uploadPhotoProfil(@RequestParam("file") MultipartFile file) {
        try {
            // Récupérer l'utilisateur connecté
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            ValidationUser utilisateur = userService.findByUsername(username);

            // Sauvegarder la photo de profil
            String cheminPhoto = photoProfilService.sauvegarderPhotoProfil(utilisateur, file);

            return ResponseEntity.ok(
                new MessageResponse("Photo de profil téléchargée avec succès", cheminPhoto)
            );
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponse("Erreur lors du téléchargement", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse(e.getMessage(), null));
        }
    }

    // Récupérer la photo de profil
    @GetMapping("/view")
    public ResponseEntity<?> viewPhotoProfil() {
        try {
            // Récupérer l'utilisateur connecté
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            ValidationUser utilisateur = userService.findByUsername(username);

            // Récupérer la photo de profil
            byte[] photoBytes = photoProfilService.recupererPhotoProfil(utilisateur);

            // Déterminer le type de média
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(photoBytes.length);

            return new ResponseEntity<>(photoBytes, headers, HttpStatus.OK);
        } catch (IOException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponse("Aucune photo de profil", null));
        }
    }

    // Supprimer la photo de profil
    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePhotoProfil() {
        try {
            // Récupérer l'utilisateur connecté
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            ValidationUser utilisateur = userService.findByUsername(username);

            // Supprimer la photo de profil
            photoProfilService.supprimerPhotoProfil(utilisateur);

            return ResponseEntity.ok(
                new MessageResponse("Photo de profil supprimée avec succès", null)
            );
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponse("Erreur lors de la suppression", null));
        }
    }

    // Endpoint admin pour voir la photo de profil d'un utilisateur
    @GetMapping("/admin/view/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> viewPhotoProfilAdmin(@PathVariable String username) {
        try {
            // Récupérer l'utilisateur
            ValidationUser utilisateur = userService.findByUsername(username);

            // Récupérer la photo de profil
            byte[] photoBytes = photoProfilService.recupererPhotoProfil(utilisateur);

            // Déterminer le type de média
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(photoBytes.length);

            return new ResponseEntity<>(photoBytes, headers, HttpStatus.OK);
        } catch (IOException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponse("Aucune photo de profil", null));
        }
    }

    // Classe interne pour les réponses
    private static class MessageResponse {
        private String message;
        private String photoPath;

        public MessageResponse(String message, String photoPath) {
            this.message = message;
            this.photoPath = photoPath;
        }

        // Getters pour la sérialisation JSON
        public String getMessage() {
            return message;
        }

        public String getPhotoPath() {
            return photoPath;
        }
    }
}
