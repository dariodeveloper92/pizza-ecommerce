package com.ecommerce.product.mappers;

import com.ecommerce.product.dto.ProductImageDTO;
import com.ecommerce.product.entities.ProductImage;
import org.springframework.stereotype.Component;

@Component
public class ProductImageMapper {

    public ProductImageDTO toDTO(ProductImage img) {
        if (img == null) return null;

        ProductImageDTO dto = new ProductImageDTO();
        dto.setId(img.getId());
        dto.setUrl(img.getUrl());
        return dto;
    }

    public ProductImage toEntity(ProductImageDTO dto) {
        if (dto == null) return null;

        ProductImage img = new ProductImage();
        img.setId(dto.getId());
        img.setUrl(dto.getUrl());
        return img;
    }
}
