package com.ecommerce.product.controllers;

import com.ecommerce.product.dto.VariantDTO;
import com.ecommerce.product.entities.Variant;
import com.ecommerce.product.mappers.VariantMapper;
import com.ecommerce.product.services.VariantService;
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
@RequestMapping("/api/variants")
@CrossOrigin(origins = "http://localhost:4200")
public class VariantController {

    private final VariantService service;
    private final VariantMapper mapper;

    public VariantController(VariantService service, VariantMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<VariantDTO>> getAll() {
        List<VariantDTO> variants = service.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();

        if (variants.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(variants);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VariantDTO> getById(@PathVariable Long id) {
        // Il Service lancia già un'eccezione se non lo trova,
        // che verrà gestita dal GlobalExceptionHandler
        return ResponseEntity.ok(mapper.toDTO(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<VariantDTO> create(@RequestBody VariantDTO dto) {
        Variant saved = service.create(mapper.toEntity(dto));
        return new ResponseEntity<>(mapper.toDTO(saved), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VariantDTO> update(@PathVariable Long id, @RequestBody VariantDTO dto) {
        Variant updated = service.update(id, mapper.toEntity(dto));
        return ResponseEntity.ok(mapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}