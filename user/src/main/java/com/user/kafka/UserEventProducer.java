package com.user.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserEventProducer {

    private static final Logger log = LoggerFactory.getLogger(UserEventProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;

    public UserEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserRegistered(Long userId, String email) {
        log.info("KAFKA: Invio evento registrazione per l'utente ID: {} (Email: {})", userId, email);
        // Inviamo una stringa semplice, ad esempio "ID;EMAIL"
        String payload = userId + ";" + email;
        kafkaTemplate.send("user-registered", payload);
    }
}