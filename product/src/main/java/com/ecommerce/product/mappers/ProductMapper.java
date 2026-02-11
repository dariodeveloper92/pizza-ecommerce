package com.ecommerce.product.mappers;

import com.ecommerce.product.dto.*;
import com.ecommerce.product.entities.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductDTO toDTO(Product p) {
        ProductDTO dto = new ProductDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setPrice(p.getPrice());
        dto.setAvailable(p.getAvailable());

        if (p.getCategory() != null) {
            CategoryDTO c = new CategoryDTO();
            c.setId(p.getCategory().getId());
            c.setName(p.getCategory().getName());
            dto.setCategory(c);
        }

        if (p.getIngredients() != null) {
            dto.setIngredients(
                    p.getIngredients().stream().map(i -> {
                        IngredientDTO d = new IngredientDTO();
                        d.setId(i.getId());
                        d.setName(i.getName());
                        return d;
                    }).collect(Collectors.toList())
            );
        }

        if (p.getVariants() != null) {
            dto.setVariants(
                    p.getVariants().stream().map(v -> {
                        VariantDTO d = new VariantDTO();
                        d.setId(v.getId());
                        d.setSize(v.getSize());
                        d.setPrice(v.getPrice());
                        return d;
                    }).collect(Collectors.toList())
            );
        }

        if (p.getImages() != null) {
            dto.setImages(
                    p.getImages().stream().map(img -> {
                        ProductImageDTO d = new ProductImageDTO();
                        d.setId(img.getId());
                        d.setUrl(img.getUrl());
                        return d;
                    }).collect(Collectors.toList())
            );
        }

        return dto;
    }

    public Product toEntity(ProductDTO dto) {
        Product p = new Product();
        p.setId(dto.getId());
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        p.setAvailable(dto.getAvailable());
        return p;
    }
}

