package com.ecommerce.product.services;

import com.ecommerce.product.entities.Category;
import com.ecommerce.product.repositories.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository repo;

    public CategoryService(CategoryRepository repo) {
        this.repo = repo;
    }

    public List<Category> findAll() {
        log.info("inizio \n");
        log.info("Richiesta ricevuta: recupero di tutte le categorie");

        List<Category> categories = repo.findAll();
        log.info("Trovate {} categorie nel database", categories.size());

        categories.forEach(c -> log.info(" -> Categoria: [ID: {} | Nome: {}]", c.getId(), c.getName()));

        log.info("fine \n");
        return categories;
    }

    public Category findById(Long id) {
        log.info("inizio \n");
        log.info("Ricerca categoria con ID: {}", id);

        Category category = repo.findById(id)
                .orElseThrow(() -> {
                    log.error("ERRORE: Categoria con ID {} non trovata!", id);
                    return new RuntimeException("Categoria non trovata: " + id);
                });

        log.info("Categoria trovata: {}", category.getName());
        log.info("fine \n");
        return category;
    }

    @Transactional
    public Category create(Category c) {
        log.info("inizio \n");
        log.info("Tentativo di creazione nuova categoria: {}", c.getName());

        Category saved = repo.save(c);
        log.info("Categoria salvata con successo! ID: {}", saved.getId());

        log.info("fine \n");
        return saved;
    }

    @Transactional
    public Category update(Long id, Category c) {
        log.info("inizio \n");
        log.info("Tentativo di aggiornamento categoria ID: {}", id);

        // findById gestisce già i log di ricerca e di errore
        Category existing = findById(id);

        log.info("Aggiornamento nome categoria: da '{}' a '{}'", existing.getName(), c.getName());
        existing.setName(c.getName());

        Category updated = repo.save(existing);
        log.info("Aggiornamento completato per la categoria ID: {}", updated.getId());

        log.info("fine \n");
        return updated;
    }

    @Transactional
    public void delete(Long id) {
        log.info("inizio \n");
        log.info("Richiesta eliminazione categoria ID: {}", id);

        if (!repo.existsById(id)) {
            log.error("ERRORE: Eliminazione fallita. La categoria ID {} non esiste", id);
            throw new RuntimeException("Impossibile eliminare: Categoria ID " + id + " non esistente");
        }

        repo.deleteById(id);
        log.info("Categoria ID: {} eliminata con successo", id);
        log.info("fine \n");
    }
}