package com.exemple.Cloud.Service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.exemple.Cloud.DAO.UserDAO;
import com.exemple.Cloud.Model.User;

@Service
public class AuthService {

    @Autowired
    private UserDAO  userDAO;

    @Autowired 
    private PasswordEncoder passwordEncoder;

    public User registerUser(String name,String email, String password){
        User user = new User();
        user.setUsername(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoleId(2);
        return userDAO.save(user);
    }
    public Optional<User> ModifierProfilImage(Long userId, String imagePath) {
        Optional<User> optionalUser = userDAO.findById(userId);
    
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
    
            if (imagePath != null && !imagePath.isEmpty()) {
                System.out.println("✅ Mise à jour de l'image pour l'utilisateur ID: " + userId);
                System.out.println("📂 Nouveau chemin de l'image : " + imagePath);
    
                user.setImage(imagePath);
                userDAO.save(user); // Sauvegarde des modifications
    
                return Optional.of(user);
            } else {
                System.out.println("❌ Image invalide !");
            }
        } else {
            System.out.println("❌ Utilisateur non trouvé avec ID : " + userId);
        }
    
        return Optional.empty(); // Retourne vide si l'utilisateur n'existe pas
    }
    public Optional<User> login(String email, String password) {
        Optional<User> userOptional = userDAO.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

    public Optional<User> findUserByEmail(String email) {
        return userDAO.findByEmail(email);
    }
    
}
