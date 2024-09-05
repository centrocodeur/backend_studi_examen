package com.marien.jwt.backend.services.imp;

import com.marien.jwt.backend.entities.User;
import com.marien.jwt.backend.entities.Validation;
import com.marien.jwt.backend.repositories.ValidationRepository;
import com.marien.jwt.backend.services.NotificationService;
import com.marien.jwt.backend.services.ValidationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import static java.time.temporal.ChronoUnit.MINUTES;

@Slf4j
@AllArgsConstructor
@Service
public class ValidationServiceImpl implements ValidationService {

    private ValidationRepository validationRepository;
    private NotificationService notificationService;

    @Override
    public void recordUser(User user) {
        Validation validation =new Validation();
        validation.setUser(user);
        Instant creation = Instant.now();
        validation.setCreation(creation);

        Instant expiration = creation.plus(10, MINUTES);
        validation.setExpiration(expiration);

        Random random =  new Random();
        int randonInteger= random.nextInt(999999);
        String code= String.format("%06d", randonInteger);
        validation.setCode(code);

        this.validationRepository.save(validation);
        this.notificationService.envoyer(validation);


    }

    public Validation readCode(String code){
        return this.validationRepository.findByCode(code)
                .orElseThrow(()-> new RuntimeException("Invalid code"));
    }



}
