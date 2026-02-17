package com.cart.controllers;

import com.cart.entities.CartItem;
import com.cart.services.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    // GET http://localhost:8080/api/cart
    // L'email arriva dal Gateway nell'header
    @GetMapping
    public ResponseEntity<List<CartItem>> getMyCart(@RequestHeader("X-User-Email") String email) {
        System.out.println("Cart: Recupero carrello per " + email);
        return ResponseEntity.ok(service.getCartByUser(email));
    }

    // POST http://localhost:8080/api/cart
    @PostMapping
    public ResponseEntity<CartItem> add(@RequestBody CartItem item, @RequestHeader("X-User-Email") String email) {
        item.setUserEmail(email);
        return ResponseEntity.ok(service.addToCart(item));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        service.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}