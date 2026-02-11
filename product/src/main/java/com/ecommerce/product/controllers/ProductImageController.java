package com.ecommerce.product.controllers;

import com.ecommerce.product.dto.ProductImageDTO;
import com.ecommerce.product.entities.ProductImage;
import com.ecommerce.product.mappers.ProductImageMapper;
import com.ecommerce.product.services.ProductImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductImageController {

    private final ProductImageService service;
    private final ProductImageMapper mapper;

    public ProductImageController(ProductImageService service, ProductImageMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<ProductImageDTO>> getAll() {
        List<ProductImageDTO> images = service.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();

        if (images.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(images);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductImageDTO> getById(@PathVariable Long id) {
        // Il service gestisce già il lancio dell'eccezione se l'ID non esiste
        return ResponseEntity.ok(mapper.toDTO(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ProductImageDTO> create(@RequestBody ProductImageDTO dto) {
        ProductImage saved = service.create(mapper.toEntity(dto));
        return new ResponseEntity<>(mapper.toDTO(saved), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductImageDTO> update(@PathVariable Long id, @RequestBody ProductImageDTO dto) {
        ProductImage updated = service.update(id, mapper.toEntity(dto));
        return ResponseEntity.ok(mapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}