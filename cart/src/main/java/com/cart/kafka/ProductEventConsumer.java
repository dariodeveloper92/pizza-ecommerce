package com.cart.kafka;

import com.cart.repositories.CartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ProductEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(ProductEventConsumer.class);
    private final CartRepository cartRepository;

    public ProductEventConsumer(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Transactional
    @KafkaListener(topics = "product-deleted", groupId = "cart-group")
    public void handleProductDeleted(String productId) {
        log.info("KAFKA: Ricevuto evento eliminazione prodotto ID: {}. Sincronizzo i carrelli...", productId);

        try {
            Long id = Long.parseLong(productId);
            // ESEGUE L'ELIMINAZIONE REALE NEL DB DEL CARRELLO
            cartRepository.deleteByProductId(id);
            log.info("KAFKA: Pulizia carrelli completata per prodotto ID: {}", id);
        } catch (NumberFormatException e) {
            log.error("KAFKA: Errore nel formato dell'ID ricevuto: {}", productId);
        }
    }
}