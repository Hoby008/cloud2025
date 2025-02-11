package com.exemple.Cloud.Controller;  

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.exemple.Cloud.DAO.UserDAO;
import com.exemple.Cloud.Model.Portefeuille;
import com.exemple.Cloud.Model.Transaction;
import com.exemple.Cloud.Model.User;
import com.exemple.Cloud.Service.CriptoService;

@RestController
@RequestMapping("/api/crypto")
public class ControllerCrypto {

    @Autowired
    private CriptoService cryptoService;

    @Autowired
    private UserDAO userDAO;
    
    // Récupération automatique de l'utilisateur connecté
    @GetMapping("/portefeuille")
    public Portefeuille getPortefeuille() {
        User user = getAuthenticatedUser();
        return cryptoService.getPortefeuille(user);
    }

    @PostMapping("/transaction")
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        User user = getAuthenticatedUser();
        return cryptoService.createTransaction(
            transaction.getType(),
            transaction.getCryptomonnaieId(),
            transaction.getMontant(),
            transaction.getPrix(),
            user
        );
    }

    @GetMapping("/historique")
    public List<Transaction> getHistorique() {
        User user = getAuthenticatedUser();
        return cryptoService.getHistorique(user.getId());
    }
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Supposons que l'email est utilisé comme identifiant
        return userDAO.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));
    }
}
