package com.projects.edustore.mapper;

import com.projects.edustore.dto.productDto.ProductRequestDto;
import com.projects.edustore.dto.productDto.ProductCustomerResponseDto;
import com.projects.edustore.dto.productDto.ProductStudentResponseDto;
import com.projects.edustore.model.person.StudentProfile;
import com.projects.edustore.model.products.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public static Product toEntity(ProductRequestDto dto, StudentProfile student){
        Product product = new Product();

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setCostPrice(dto.getCostPrice());
        product.setMaker(student);
        return product;
    }

    public static void updateProduct(Product existing, ProductRequestDto dto) {

        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getPrice() != null) existing.setPrice(dto.getPrice());
        if (dto.getStockQuantity() != null) existing.setStockQuantity(dto.getStockQuantity());

        }

    public static ProductCustomerResponseDto toCustomerResponseDto(Product product){
        ProductCustomerResponseDto dto = new ProductCustomerResponseDto();

        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());

        return dto;
    }

    public static ProductStudentResponseDto toStudentResponseDto(Product product){
        ProductStudentResponseDto dto = new ProductStudentResponseDto();

        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());

        dto.setStockQuanity(product.getStockQuantity());
        dto.setCostPrice(product.getCostPrice());
        dto.setMakerId(product.getMaker().getId());
        return dto;
    }

}
