package com.ecommerce.product.services;

import com.ecommerce.product.entities.ProductImage;
import com.ecommerce.product.repositories.ProductImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductImageService {

    private static final Logger log = LoggerFactory.getLogger(ProductImageService.class);
    private final ProductImageRepository repo;

    public ProductImageService(ProductImageRepository repo) {
        this.repo = repo;
    }

    public List<ProductImage> findAll() {
        log.info("inizio \n");
        log.info("Richiesta ricevuta: recupero di tutti i link alle immagini dei prodotti");

        List<ProductImage> images = repo.findAll();
        log.info("Trovate {} immagini nel database", images.size());

        images.forEach(img -> log.info(" -> Immagine: [ID: {} | URL: {}]", img.getId(), img.getUrl()));

        log.info("fine \n");
        return images;
    }

    public ProductImage findById(Long id) {
        log.info("inizio \n");
        log.info("Ricerca immagine con ID: {}", id);

        ProductImage image = repo.findById(id)
                .orElseThrow(() -> {
                    log.error("ERRORE: Immagine con ID {} non trovata!", id);
                    return new RuntimeException("Immagine non trovata: " + id);
                });

        log.info("Immagine trovata. URL: {}", image.getUrl());
        log.info("fine \n");
        return image;
    }

    @Transactional
    public ProductImage create(ProductImage img) {
        log.info("inizio \n");
        log.info("Tentativo di aggiunta nuova immagine. URL: {}", img.getUrl());

        ProductImage saved = repo.save(img);
        log.info("Immagine salvata con successo! ID assegnato: {}", saved.getId());

        log.info("fine \n");
        return saved;
    }

    @Transactional
    public ProductImage update(Long id, ProductImage img) {
        log.info("inizio \n");
        log.info("Tentativo di aggiornamento immagine ID: {}", id);

        // findById gestisce già i log di ricerca e di errore
        ProductImage existing = findById(id);

        log.info("Modifica URL immagine: da '{}' a '{}'", existing.getUrl(), img.getUrl());
        existing.setUrl(img.getUrl());

        ProductImage updated = repo.save(existing);
        log.info("Aggiornamento completato per l'immagine ID: {}", updated.getId());

        log.info("fine \n");
        return updated;
    }

    @Transactional
    public void delete(Long id) {
        log.info("inizio \n");
        log.info("Richiesta di rimozione immagine ID: {}", id);

        if (!repo.existsById(id)) {
            log.error("ERRORE: Eliminazione fallita. L'immagine ID {} non esiste", id);
            throw new RuntimeException("Impossibile eliminare: Immagine ID " + id + " non esistente");
        }

        repo.deleteById(id);
        log.info("Immagine ID: {} rimossa correttamente", id);
        log.info("fine \n");
    }
}