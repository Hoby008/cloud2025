package com.exemple.Cloud.Service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exemple.Cloud.DAO.PortefeuilleRepository;
import com.exemple.Cloud.DAO.TransactionRepository;
import com.exemple.Cloud.DAO.UserDAO;
import com.exemple.Cloud.Model.Portefeuille;
import com.exemple.Cloud.Model.Transaction;
import com.exemple.Cloud.Model.User;

@Service
public class CriptoService {

    @Autowired
    private PortefeuilleRepository portefeuilleRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // Récupérer un portefeuille, ou en créer un si inexistant
    public Portefeuille getPortefeuille(User user) {
        return portefeuilleRepository.findByUser(user)
                .orElseGet(() -> {
                    Portefeuille newPortefeuille = new Portefeuille();
                    newPortefeuille.setUser(user);
                    newPortefeuille.setSolde(BigDecimal.ZERO);
                    return portefeuilleRepository.save(newPortefeuille);
                });
    }

    // Créer une transaction (dépôt, retrait, achat, vente)
    public Transaction createTransaction(String type, Long cryptomonnaieId, BigDecimal montant, BigDecimal prix, User user) {
        // Récupérer le portefeuille de l'utilisateur
        Portefeuille portefeuille = getPortefeuille(user);
        BigDecimal nouveauSolde = portefeuille.getSolde();

        // Traiter la transaction selon son type
        if ("depot".equalsIgnoreCase(type)) {
            nouveauSolde = nouveauSolde.add(montant);  // Dépôt
        } else if ("retrait".equalsIgnoreCase(type) && nouveauSolde.compareTo(montant) >= 0) {
            nouveauSolde = nouveauSolde.subtract(montant);  // Retrait
        } else if ("achat".equalsIgnoreCase(type) || "vente".equalsIgnoreCase(type)) {
            // Calculer le coût total pour un achat ou la valeur pour une vente
            BigDecimal totalCost = montant.multiply(prix);  // Coût total d'achat ou valeur de vente
            if ("achat".equalsIgnoreCase(type) && nouveauSolde.compareTo(totalCost) >= 0) {
                nouveauSolde = nouveauSolde.subtract(totalCost);  // Acheter
            } else if ("vente".equalsIgnoreCase(type)) {
                nouveauSolde = nouveauSolde.add(totalCost);  // Vendre
            } else {
                throw new RuntimeException("Solde insuffisant pour l'achat.");
            }
        } else {
            throw new RuntimeException("Type de transaction invalide.");
        }

        // Mettre à jour le solde du portefeuille
        portefeuille.setSolde(nouveauSolde);
        portefeuilleRepository.save(portefeuille);

        // Créer et enregistrer la transaction
        Transaction transaction = new Transaction();
        transaction.setType(type);
        transaction.setCryptomonnaieId(cryptomonnaieId);
        transaction.setMontant(montant);
        transaction.setPrix(prix);
        transaction.setUser(user);  // Associer l'utilisateur à la transaction
        return transactionRepository.save(transaction);
    }

    @Autowired
    private UserDAO userDAO;  // Assurez-vous d'avoir un repository pour User

    public List<Transaction> getHistorique(Long userId) {
        User user = userDAO.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return transactionRepository.findByUser(user);
    }
}
