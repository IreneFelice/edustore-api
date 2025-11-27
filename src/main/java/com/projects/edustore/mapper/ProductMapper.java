package com.projects.edustore.mapper;

import com.projects.edustore.dto.productDto.ProductRequestDto;
import com.projects.edustore.dto.productDto.ProductCustomerResponseDto;
import com.projects.edustore.model.products.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public static Product toEntity(ProductRequestDto dto){
        Product product = new Product();

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setCostPrice(dto.getCostPrice());
        product.setStockType(dto.getStockType());
        return product;
    }

    public static ProductCustomerResponseDto toResponseDto(Product product){
        ProductCustomerResponseDto dto = new ProductCustomerResponseDto();

        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());

        return dto;
    }
}
