package com.ecommerce.product.repositories;

import com.ecommerce.product.entities.Variant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VariantRepository extends JpaRepository<Variant, Long> {

}
