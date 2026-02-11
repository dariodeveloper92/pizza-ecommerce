package com.ecommerce.product.controllers;

import com.ecommerce.product.dto.IngredientDTO;
import com.ecommerce.product.entities.Ingredient;
import com.ecommerce.product.mappers.IngredientMapper;
import com.ecommerce.product.services.IngredientService;
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
@RequestMapping("/api/ingredients")
@CrossOrigin(origins = "http://localhost:4200")
public class IngredientController {

    private final IngredientService service;
    private final IngredientMapper mapper;

    public IngredientController(IngredientService service, IngredientMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<IngredientDTO>> getAll() {
        List<IngredientDTO> list = service.findAll().stream()
                .map(mapper::toDTO)
                .toList();
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDTO(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<IngredientDTO> create(@RequestBody IngredientDTO dto) {
        Ingredient saved = service.create(mapper.toEntity(dto));
        return new ResponseEntity<>(mapper.toDTO(saved), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientDTO> update(@PathVariable Long id, @RequestBody IngredientDTO dto) {
        Ingredient updated = service.update(id, mapper.toEntity(dto));
        return ResponseEntity.ok(mapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
