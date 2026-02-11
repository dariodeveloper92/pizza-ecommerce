package com.ecommerce.product.mappers;

import com.ecommerce.product.dto.CategoryDTO;
import com.ecommerce.product.entities.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDTO toDTO(Category c) {
        if (c == null) return null;

        CategoryDTO dto = new CategoryDTO();
        dto.setId(c.getId());
        dto.setName(c.getName());
        return dto;
    }

    public Category toEntity(CategoryDTO dto) {
        if (dto == null) return null;

        Category c = new Category();
        c.setId(dto.getId());
        c.setName(dto.getName());
        return c;
    }
}

