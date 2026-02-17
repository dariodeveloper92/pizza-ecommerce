package com.cart.repositories;

import com.cart.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CartRepository extends JpaRepository<CartItem, Long> {
    // Trova tutti gli elementi nel carrello di un utente specifico
    List<CartItem> findByUserEmail(String userEmail);

    // Utile se vuoi svuotare il carrello dopo un ordine
    void deleteByUserEmail(String userEmail);
}