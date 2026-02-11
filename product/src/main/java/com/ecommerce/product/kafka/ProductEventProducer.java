package com.ecommerce.product.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductEventProducer {

    private static final Logger log = LoggerFactory.getLogger(ProductEventProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;

    public ProductEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // Notifica quando un prodotto viene creato
    public void sendProductCreated(Long productId) {
        log.info("KAFKA: Invio evento creazione per prodotto ID: {}", productId);
        kafkaTemplate.send("product-created", productId.toString());
    }

    // Notifica quando un prodotto viene eliminato (Fondamentale per il Carrello!)
    public void sendProductDeleted(Long productId) {
        log.info("KAFKA: Invio evento eliminazione per prodotto ID: {}", productId);
        kafkaTemplate.send("product-deleted", productId.toString());
    }

    // Notifica quando il prezzo cambia o la disponibilità varia
    public void sendProductUpdated(Long productId) {
        log.info("KAFKA: Invio evento aggiornamento per prodotto ID: {}", productId);
        kafkaTemplate.send("product-updated", productId.toString());
    }
}