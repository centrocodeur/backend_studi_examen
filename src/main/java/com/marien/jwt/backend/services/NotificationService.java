package com.marien.jwt.backend.services;

import com.marien.jwt.backend.entities.Validation;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NotificationService {


    JavaMailSender javaMailSender;
    public void envoyer(Validation validation) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("centrocodeur@gmail.com");
        message.setTo(validation.getUser().getEmail());
        message.setSubject("Votre code d'activation");

        String texte = String.format(
                "Bonjour %s, <br /> Votre code d'action est %s; A bientôt",
                validation.getUser().getFirstName(),
                validation.getCode()
        );
        message.setText(texte);

        javaMailSender.send(message);
    }

}
