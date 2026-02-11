package com.ecommerce.product.dto;

import lombok.Data;
import java.util.List;

@Data
public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private Boolean available;

    private CategoryDTO category;
    private List<IngredientDTO> ingredients;
    private List<VariantDTO> variants;
    private List<ProductImageDTO> images;
}

