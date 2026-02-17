package com.cart.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userEmail; // Ricevuto dal Gateway via Header

    @Column(nullable = false)
    private Long productId;   // ID della pizza dal microservizio Product

    @Column(nullable = false)
    private Integer quantity;

    // Se vuoi salvare anche dati che cambiano raramente per non fare troppe JOIN
    private String productName;
    private Double unitPrice;
}