package com.ecommerce.product.mappers;

import com.ecommerce.product.dto.VariantDTO;
import com.ecommerce.product.entities.Variant;
import org.springframework.stereotype.Component;

@Component
public class VariantMapper {

    public VariantDTO toDTO(Variant v) {
        if (v == null) return null;

        VariantDTO dto = new VariantDTO();
        dto.setId(v.getId());
        dto.setSize(v.getSize());
        dto.setPrice(v.getPrice());
        return dto;
    }

    public Variant toEntity(VariantDTO dto) {
        if (dto == null) return null;

        Variant v = new Variant();
        v.setId(dto.getId());
        v.setSize(dto.getSize());
        v.setPrice(dto.getPrice());
        return v;
    }
}
