package com.notification.kafka;

import com.notification.entities.NotificationLog;
import com.notification.repositories.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NotificationConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);
    private final NotificationRepository repository;

    public NotificationConsumer(NotificationRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "user-registered", groupId = "notification-group")
    public void handleUserRegistration(String message) {
        String email = message.split(";")[1];

        NotificationLog entry = new NotificationLog();
        entry.setRecipient(email);
        entry.setMessage("Welcome email sent for registration");
        entry.setType("WELCOME");
        entry.setSentAt(LocalDateTime.now());

        repository.save(entry);
        log.info("📧 Notifica salvata nel DB per: {}", email);
    }

    @KafkaListener(topics = "product-deleted", groupId = "notification-group")
    public void handleProductDeleted(String productId) {
        NotificationLog entry = new NotificationLog();
        entry.setRecipient("ADMIN"); // In questo caso il destinatario è il sistema/admin
        entry.setMessage("Product with ID " + productId + " was deleted from catalog.");
        entry.setType("PRODUCT_REMOVAL");
        entry.setSentAt(LocalDateTime.now());

        repository.save(entry);
        log.info("⚠️ Notifica di eliminazione salvata per il prodotto ID: {}", productId);
    }
}