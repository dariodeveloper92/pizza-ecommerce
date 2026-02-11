package com.ecommerce.product.services;

import com.ecommerce.product.entities.Product;
import com.ecommerce.product.kafka.ProductEventProducer;
import com.ecommerce.product.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository repo;
    private final ProductEventProducer eventProducer;

    //DI - Costruttore
    public ProductService(ProductRepository repo, ProductEventProducer eventProducer) {
        this.repo = repo;
        this.eventProducer = eventProducer;
    }

    public List<Product> findAll() {
        log.info("inizio \n ");
        log.info("Richiesta ricevuta: recupero di tutti i prodotti dal database");
        List<Product> products = repo.findAll();

        log.info("Trovati {} prodotti nel database", products.size());

        // Logghiamo i dettagli di ogni prodotto trovato
        products.forEach(p -> log.info(" -> Prodotto recuperato: [ID: {} | Nome: {} | Prezzo: {}€ | Disponibile: {}] ",
                p.getId(), p.getName(), p.getPrice(), p.getAvailable()));
        log.info("fine \n ");

        return products;
    }

    public Product findById(Long id) {
        log.info("inizio \n ");
        log.info("Ricerca prodotto con ID: {}", id);

        Product product = repo.findById(id)
                .orElseThrow(() -> {
                    log.error("ERRORE: Prodotto con ID {} non trovato!", id);
                    return new RuntimeException("Prodotto non trovato");
                });

        log.info("Prodotto trovato: {}", product.getName());
        log.info("fine \n ");
        return product;
    }

    @Transactional
    public Product create(Product p) {
        log.info("inizio \n ");
        log.info("Tentativo di creazione nuovo prodotto: {}", p.getName());
        Product saved = repo.save(p);

        // NOTIFICA KAFKA: Prodotto creato
        eventProducer.sendProductCreated(saved.getId());

        log.info("Prodotto salvato con successo! ID assegnato: {}", saved.getId());
        log.info("fine \n ");
        return saved;
    }

    @Transactional
    public Product update(Long id, Product p) {
        log.info("inizio \n ");
        log.info("Tentativo di aggiornamento prodotto ID: {}", id);

        // findById contiene già il log.info e il log.error in caso di fallimento
        Product existing = findById(id);

        // OPZIONALE: Notifica Kafka per l'aggiornamento
         eventProducer.sendProductUpdated(id);

        log.info("Prodotto trovato. Aggiornamento dati da '{}' a '{}'", existing.getName(), p.getName());

        existing.setName(p.getName());
        existing.setDescription(p.getDescription());
        existing.setPrice(p.getPrice());
        existing.setAvailable(p.getAvailable());

        Product updated = repo.save(existing);
        log.info("Aggiornamento completato con successo per il prodotto ID: {}", updated.getId());
        log.info("fine \n ");
        return updated;
    }

    @Transactional
    public void delete(Long id) {
        log.info("inizio \n ");
        log.info("Ricevuta richiesta di eliminazione per il prodotto ID: {}", id);

        if (!repo.existsById(id)) {
            log.error("ERRORE: Eliminazione fallita. Il prodotto ID {} non esiste nel database", id);
            throw new RuntimeException("Impossibile eliminare: Prodotto con ID " + id + " non esistente");
        }
        repo.deleteById(id);

        // NOTIFICA KAFKA
        eventProducer.sendProductDeleted(id);

        log.info("Prodotto ID: {} eliminato correttamente dal database", id);
        log.info("fine \n ");
    }
}