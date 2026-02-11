package com.ecommerce.product.services;

import com.ecommerce.product.entities.Variant;
import com.ecommerce.product.repositories.VariantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VariantService {

    private static final Logger log = LoggerFactory.getLogger(VariantService.class);
    private final VariantRepository repo;

    public VariantService(VariantRepository repo) {
        this.repo = repo;
    }

    public List<Variant> findAll() {
        log.info("inizio \n");
        log.info("Richiesta ricevuta: recupero di tutte le varianti di prodotto");

        List<Variant> variants = repo.findAll();
        log.info("Trovate {} varianti nel database", variants.size());

        variants.forEach(v -> log.info(" -> Variante: [ID: {} | Formato: {} | Prezzo: {}€]",
                v.getId(), v.getSize(), v.getPrice()));

        log.info("fine \n");
        return variants;
    }

    public Variant findById(Long id) {
        log.info("inizio \n");
        log.info("Ricerca variante con ID: {}", id);

        Variant variant = repo.findById(id)
                .orElseThrow(() -> {
                    log.error("ERRORE: Variante con ID {} non trovata!", id);
                    return new RuntimeException("Variante non trovata: " + id);
                });

        log.info("Variante trovata: [Formato: {} | Prezzo: {}€]", variant.getSize(), variant.getPrice());
        log.info("fine \n");
        return variant;
    }

    @Transactional
    public Variant create(Variant v) {
        log.info("inizio \n");
        log.info("Tentativo di creazione nuova variante: {} a {}€", v.getSize(), v.getPrice());

        Variant saved = repo.save(v);
        log.info("Variante salvata con successo! ID assegnato: {}", saved.getId());

        log.info("fine \n");
        return saved;
    }

    @Transactional
    public Variant update(Long id, Variant v) {
        log.info("inizio \n");
        log.info("Tentativo di aggiornamento variante ID: {}", id);

        // findById gestisce già i log di ricerca e di errore
        Variant existing = findById(id);

        log.info("Modifica dati variante: Formato '{}'->'{}', Prezzo '{}'->'{}'",
                existing.getSize(), v.getSize(), existing.getPrice(), v.getPrice());

        existing.setSize(v.getSize());
        existing.setPrice(v.getPrice());

        Variant updated = repo.save(existing);
        log.info("Aggiornamento completato per la variante ID: {}", updated.getId());

        log.info("fine \n");
        return updated;
    }

    @Transactional
    public void delete(Long id) {
        log.info("inizio \n");
        log.info("Richiesta di eliminazione variante ID: {}", id);

        if (!repo.existsById(id)) {
            log.error("ERRORE: Eliminazione fallita. La variante ID {} non esiste", id);
            throw new RuntimeException("Impossibile eliminare: Variante ID " + id + " non esistente");
        }

        repo.deleteById(id);
        log.info("Variante ID: {} rimosssa con successo dal sistema", id);
        log.info("fine \n");
    }
}