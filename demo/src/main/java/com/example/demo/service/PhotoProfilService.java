package com.example.demo.service;

import com.example.demo.entity.ValidationUser;
import com.example.demo.repository.ValidationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class PhotoProfilService {

    @Autowired
    private ValidationUserRepository utilisateurRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public String sauvegarderPhotoProfil(ValidationUser utilisateur, MultipartFile fichier) throws IOException {
        // Vérifier si un fichier est fourni
        if (fichier.isEmpty()) {
            throw new IllegalArgumentException("Aucun fichier téléchargé");
        }

        // Vérifier le type de fichier
        String contentType = fichier.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Le fichier doit être une image");
        }

        // Générer un nom de fichier unique
        String extension = getExtension(fichier.getOriginalFilename());
        String nomFichier = UUID.randomUUID().toString() + "." + extension;

        // Créer le répertoire de téléchargement s'il n'existe pas
        Path uploadPath = Paths.get(uploadDir, "photos-profil");
        Files.createDirectories(uploadPath);

        // Chemin complet du fichier
        Path cheminFichier = uploadPath.resolve(nomFichier);

        // Copier le fichier
        Files.copy(fichier.getInputStream(), cheminFichier, StandardCopyOption.REPLACE_EXISTING);

        // Supprimer l'ancienne photo si elle existe
        if (utilisateur.getPhotoProfilPath() != null) {
            try {
                Files.deleteIfExists(Paths.get(utilisateur.getPhotoProfilPath()));
            } catch (IOException e) {
                // Log de l'erreur sans interrompre le processus
            }
        }

        // Mettre à jour le chemin de la photo de profil
        String cheminRelatif = "photos-profil/" + nomFichier;
        utilisateur.setPhotoProfilPath(cheminRelatif);
        utilisateurRepository.save(utilisateur);

        return cheminRelatif;
    }

    public byte[] recupererPhotoProfil(ValidationUser utilisateur) throws IOException {
        if (utilisateur.getPhotoProfilPath() == null) {
            throw new IllegalArgumentException("Aucune photo de profil");
        }

        Path cheminFichier = Paths.get(uploadDir, utilisateur.getPhotoProfilPath());
        return Files.readAllBytes(cheminFichier);
    }

    public void supprimerPhotoProfil(ValidationUser utilisateur) throws IOException {
        if (utilisateur.getPhotoProfilPath() != null) {
            Path cheminFichier = Paths.get(uploadDir, utilisateur.getPhotoProfilPath());
            Files.deleteIfExists(cheminFichier);

            utilisateur.setPhotoProfilPath(null);
            utilisateurRepository.save(utilisateur);
        }
    }

    // Méthode utilitaire pour extraire l'extension du fichier
    private String getExtension(String nomFichier) {
        if (nomFichier == null) {
            return "jpg"; // Extension par défaut
        }
        
        int pointIndex = nomFichier.lastIndexOf('.');
        return (pointIndex == -1) ? "jpg" : nomFichier.substring(pointIndex + 1).toLowerCase();
    }
}
