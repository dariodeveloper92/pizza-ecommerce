package com.ecommerce.product.repositories;

import com.ecommerce.product.entities.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @EntityGraph(attributePaths = {
            "ingredients",
            "variants",
            "images",
            "category"
    })
    List<Product> findAll();

    @EntityGraph(attributePaths = {
            "ingredients",
            "variants",
            "images",
            "category"
    })
    Optional<Product> findById(Long id);

}
