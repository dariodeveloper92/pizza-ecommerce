package com.ecommerce.product.mappers;

import com.ecommerce.product.dto.IngredientDTO;
import com.ecommerce.product.entities.Ingredient;
import org.springframework.stereotype.Component;

@Component
public class IngredientMapper {

    public IngredientDTO toDTO(Ingredient i) {
        if (i == null) return null;

        IngredientDTO dto = new IngredientDTO();
        dto.setId(i.getId());
        dto.setName(i.getName());
        return dto;
    }

    public Ingredient toEntity(IngredientDTO dto) {
        if (dto == null) return null;

        Ingredient i = new Ingredient();
        i.setId(dto.getId());
        i.setName(dto.getName());
        return i;
    }
}
