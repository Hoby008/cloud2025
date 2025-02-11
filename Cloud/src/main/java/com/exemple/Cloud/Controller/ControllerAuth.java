package com.exemple.Cloud.Controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.exemple.Cloud.Model.User;
import com.exemple.Cloud.Service.AuthService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class ControllerAuth {

    @Autowired
    private AuthService authService;

    @Value("${jwt.secret}")  // Ajoutez cette clé dans application.properties
    private String secretKey;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("L'email est requis");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Le mot de passe est requis");
        }

        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            return ResponseEntity.badRequest().body("Le nom est requis");
        }

        Optional<User> existingUser = authService.findUserByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("L'email est déjà utilisé");
        }

        user.setRoleId(2); 
        User savedUser = authService.registerUser(user.getUsername(), user.getEmail(), user.getPassword());

        return ResponseEntity.ok(savedUser);
    }

    @PatchMapping(value = "/modifier-image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserImage(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile imageFile) {

        try {
            String imagePath = saveImage(imageFile);
            Optional<User> updatedUser = authService.ModifierProfilImage(id, imagePath);

            return updatedUser.isPresent()  
                ? ResponseEntity.ok(updatedUser.get()) 
                : ResponseEntity.badRequest().body("{\"error\": \"Utilisateur non trouvé\"}");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Erreur lors du traitement de l'image\"}");
        }
    }

    private String saveImage(MultipartFile file) throws IOException {
        String directory = "uploads/";
        File uploadDir = new File(directory);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String filePath = directory + file.getOriginalFilename();
        File newFile = new File(filePath);
        file.transferTo(newFile);

        return filePath;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("L'email est requis");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Le mot de passe est requis");
        }

        Optional<User> userOptional = authService.login(user.getEmail(), user.getPassword());

        return userOptional
                .map(u -> {
                    // Générer le token JWT
                    String token = generateToken(u);
                    // Retourner l'utilisateur avec le token
                    return ResponseEntity.ok(new JwtResponse(token));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new JwtResponse("Identifiants invalides")));
    }

    private String generateToken(User user) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    
        return Jwts.builder()
                .setSubject(user.getEmail()) // ou user.getUsername()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 heure d'expiration
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    
}

// Classe pour la réponse contenant le token JWT
class JwtResponse {
    private String token;

    public JwtResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

