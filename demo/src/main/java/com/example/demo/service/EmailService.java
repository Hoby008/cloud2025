package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void envoyerEmailDeblocage(String to, String tokenDeblocage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@votreapplication.com");
        message.setTo(to);
        message.setSubject("Déblocage de votre compte");
        
        String lienDeblocage = "https://votreapplication.com/debloquer?token=" + tokenDeblocage;
        
        message.setText("Votre compte a été bloqué suite à trop de tentatives de validation.\n" +
                      "Pour débloquer votre compte, cliquez sur le lien suivant :\n" +
                      lienDeblocage + "\n\n" +
                      "Ce lien sera valide pendant 24 heures.");
        
        mailSender.send(message);
    }

    public void envoyerEmail(String to, String sujet, String corps) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@votreapplication.com");
        message.setTo(to);
        message.setSubject(sujet);
        message.setText(corps);
        
        mailSender.send(message);
    }
}
