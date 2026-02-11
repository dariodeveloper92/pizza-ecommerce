package com.ecommerce.product.repositories;

import com.ecommerce.product.entities.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

}
