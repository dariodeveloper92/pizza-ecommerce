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

    public CartItem addToCart(CartItem item) {
        return repo.save(item);
    }

    public void deleteItem(Long id) {
        repo.deleteById(id);
    }

    @Transactional
    public void clearCart(String email) {
        repo.deleteByUserEmail(email);
    }
}