package com.cart.services;

import com.cart.entities.CartItem;
import com.cart.repositories.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CartService {

    private final CartRepository repo;

    public CartService(CartRepository repo) {
        this.repo = repo;
    }

    public List<CartItem> getCartByUser(String email) {
        return repo.findByUserEmail(email);
    }

    @Transactional
    public CartItem addToCart(CartItem newItem) {
        // 1. Cerchiamo se l'utente ha già quel prodotto nel carrello
        List<CartItem> currentCart = repo.findByUserEmail(newItem.getUserEmail());

        return currentCart.stream()
                .filter(item -> item.getProductId().equals(newItem.getProductId()))
                .findFirst()
                .map(existingItem -> {
                    // 2. Se esiste, sommiamo la quantità
                    existingItem.setQuantity(existingItem.getQuantity() + newItem.getQuantity());
                    return repo.save(existingItem);
                })
                .orElseGet(() -> repo.save(newItem)); // 3. Se non esiste, creiamo una nuova riga
    }

    public void deleteItem(Long id) {
        repo.deleteById(id);
    }

    @Transactional
    public void clearCart(String email) {
        repo.deleteByUserEmail(email);
    }
}