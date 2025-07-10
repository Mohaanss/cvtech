package com.example.demo.service;

import com.example.demo.config.EmailConfig;
import com.example.demo.domain.Utilisateur;
import com.example.demo.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {
    
    private final EmailConfig emailConfig;
    private final UtilisateurRepository utilisateurRepository;
    private final JwtService jwtService;
    private final TemplateEngine templateEngine;
    
    // Cache temporaire pour les tokens de réinitialisation (en production, utiliser Redis)
    private final Map<String, String> resetTokens = new HashMap<>();
    
    /**
     * Configure le JavaMailSender
     */
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailConfig.getHost());
        mailSender.setPort(emailConfig.getPort());
        mailSender.setUsername(emailConfig.getUsername());
        mailSender.setPassword(emailConfig.getPassword());
        
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", emailConfig.isAuth());
        props.put("mail.smtp.starttls.enable", emailConfig.isStarttls());
        props.put("mail.debug", "true");
        
        return mailSender;
    }
    
    /**
     * Envoie un email de réinitialisation de mot de passe
     */
    public boolean sendPasswordResetEmail(String email) {
        try {
            // Vérifier que l'utilisateur existe
            Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(email);
            if (utilisateurOpt.isEmpty()) {
                return false;
            }
            
            Utilisateur utilisateur = utilisateurOpt.get();
            
            // Générer un token de réinitialisation
            String resetToken = UUID.randomUUID().toString();
            resetTokens.put(resetToken, email);
            
            // Créer le lien de réinitialisation
            String resetLink = "http://localhost:4200/reset-password?token=" + resetToken;
            
            // Préparer le contexte pour le template
            Context context = new Context();
            context.setVariable("nom", utilisateur.getEmail().split("@")[0]); // Nom simple
            context.setVariable("resetLink", resetLink);
            context.setVariable("expirationHours", 24);
            
            // Générer le contenu HTML
            String htmlContent = templateEngine.process("password-reset", context);
            
            // Envoyer l'email
            JavaMailSender mailSender = javaMailSender();
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(emailConfig.getFrom());
            helper.setTo(email);
            helper.setSubject("Réinitialisation de votre mot de passe - CVTech");
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            
            System.out.println("✅ Email de réinitialisation envoyé à: " + email);
            return true;
            
        } catch (MessagingException e) {
            System.err.println("❌ Erreur lors de l'envoi de l'email: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Valide un token de réinitialisation
     */
    public String validateResetToken(String token) {
        return resetTokens.get(token);
    }
    
    /**
     * Supprime un token après utilisation
     */
    public void removeResetToken(String token) {
        resetTokens.remove(token);
    }
    
    /**
     * Envoie un email de confirmation de changement de mot de passe
     */
    public boolean sendPasswordChangedEmail(String email) {
        try {
            Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(email);
            if (utilisateurOpt.isEmpty()) {
                return false;
            }
            
            Utilisateur utilisateur = utilisateurOpt.get();
            
            // Préparer le contexte pour le template
            Context context = new Context();
            context.setVariable("nom", utilisateur.getEmail().split("@")[0]);
            context.setVariable("date", java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm")));
            
            // Générer le contenu HTML
            String htmlContent = templateEngine.process("password-changed", context);
            
            // Envoyer l'email
            JavaMailSender mailSender = javaMailSender();
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(emailConfig.getFrom());
            helper.setTo(email);
            helper.setSubject("Votre mot de passe a été modifié - CVTech");
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            
            System.out.println("✅ Email de confirmation envoyé à: " + email);
            return true;
            
        } catch (MessagingException e) {
            System.err.println("❌ Erreur lors de l'envoi de l'email: " + e.getMessage());
            return false;
        }
    }
} 