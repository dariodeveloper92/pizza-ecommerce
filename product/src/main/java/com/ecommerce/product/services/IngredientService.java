package com.ecommerce.product.services;

import com.ecommerce.product.entities.Ingredient;
import com.ecommerce.product.repositories.IngredientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IngredientService {

    private static final Logger log = LoggerFactory.getLogger(IngredientService.class);
    private final IngredientRepository repo;

    public IngredientService(IngredientRepository repo) {
        this.repo = repo;
    }

    public List<Ingredient> findAll() {
        log.info("inizio \n");
        log.info("Richiesta ricevuta: recupero della lista completa ingredienti");

        List<Ingredient> ingredients = repo.findAll();
        log.info("Trovati {} ingredienti nel database", ingredients.size());

        ingredients.forEach(i -> log.info(" -> Ingrediente: [ID: {} | Nome: {}]", i.getId(), i.getName()));

        log.info("fine \n");
        return ingredients;
    }

    public Ingredient findById(Long id) {
        log.info("inizio \n");
        log.info("Ricerca ingrediente con ID: {}", id);

        Ingredient ingredient = repo.findById(id)
                .orElseThrow(() -> {
                    log.error("ERRORE: Ingrediente con ID {} non trovato!", id);
                    return new RuntimeException("Ingrediente non trovato: " + id);
                });

        log.info("Ingrediente trovato: {}", ingredient.getName());
        log.info("fine \n");
        return ingredient;
    }

    @Transactional
    public Ingredient create(Ingredient i) {
        log.info("inizio \n");
        log.info("Tentativo di inserimento nuovo ingrediente: {}", i.getName());

        Ingredient saved = repo.save(i);
        log.info("Ingrediente salvato con successo! ID assegnato: {}", saved.getId());

        log.info("fine \n");
        return saved;
    }

    @Transactional
    public Ingredient update(Long id, Ingredient i) {
        log.info("inizio \n");
        log.info("Tentativo di aggiornamento ingrediente ID: {}", id);

        // findById gestisce già i log di ricerca e di errore
        Ingredient existing = findById(id);

        log.info("Modifica nome ingrediente: da '{}' a '{}'", existing.getName(), i.getName());
        existing.setName(i.getName());

        Ingredient updated = repo.save(existing);
        log.info("Aggiornamento completato per l'ingrediente ID: {}", updated.getId());

        log.info("fine \n");
        return updated;
    }

    @Transactional
    public void delete(Long id) {
        log.info("inizio \n");
        log.info("Richiesta di eliminazione ingrediente ID: {}", id);

        if (!repo.existsById(id)) {
            log.error("ERRORE: Eliminazione fallita. L'ingrediente ID {} non esiste", id);
            throw new RuntimeException("Impossibile eliminare: Ingrediente ID " + id + " non esistente");
        }

        repo.deleteById(id);
        log.info("Ingrediente ID: {} rimosso correttamente dal database", id);
        log.info("fine \n");
    }
}