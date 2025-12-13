package com.projects.edustore.service;

import com.projects.edustore.dto.OrderDto.CartRequestDto;
import com.projects.edustore.dto.OrderDto.CartResponseDto;
import com.projects.edustore.dto.OrderDto.OrderCustomerResponseDto;
import com.projects.edustore.dto.productDto.ProductCustomerResponseDto;
import com.projects.edustore.dto.productDto.ProductRequestDto;
import com.projects.edustore.dto.productDto.ProductStudentResponseDto;
import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.mapper.CartItemMapper;
import com.projects.edustore.mapper.ProductMapper;
import com.projects.edustore.model.Role;
import com.projects.edustore.model.User;
import com.projects.edustore.model.products.CartItem;
import com.projects.edustore.model.products.Product;
import com.projects.edustore.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repos;
    private final WhoCanSeeWhoService whoCanSee;

    public ProductService (ProductRepository repos, WhoCanSeeWhoService whoCanSee) {
        this.repos =repos;
        this.whoCanSee = whoCanSee;
    }

    public List<ProductCustomerResponseDto> getAllProducts() {
        List<Product> products = repos.findAll();
        List<ProductCustomerResponseDto> dtos = new ArrayList<>();
        for (Product product : products) {
            dtos.add(ProductMapper.toCustomerResponseDto(product));
        }
        return dtos;
    }

    public List<ProductCustomerResponseDto> getProductsBySchoolPeriod(String schoolPeriod) {
        List<Product> products = repos.findByMaker_SchoolPeriod(schoolPeriod);
        List<ProductCustomerResponseDto> dtos = new ArrayList<>();
        for(Product product : products) {
            dtos.add(ProductMapper.toCustomerResponseDto(product));
        }
                return dtos;
    }


//
//    public OrderCustomerResponseDto createOrderFromCart(OrderCustomerRequestDto orderCustomerRequestDto) {
//return OrderCustomerResponseDto;



    public ProductStudentResponseDto createNewProduct(ProductRequestDto dto, Long id) {
        User user = findStudent(id);
                Product newProduct = ProductMapper.toEntity(dto, user.getPerson().getStudentProfile());

        repos.save(newProduct);
        return ProductMapper.toStudentResponseDto(newProduct);
    }

    public List<ProductStudentResponseDto> getProductsByMaker(Long id) {
        findStudent(id);
        List<Product> products = repos.findByMakerId(id);
        List<ProductStudentResponseDto> dtos = new ArrayList<>();

        for(Product product : products) {
            dtos.add(ProductMapper.toStudentResponseDto(product));
        }
            return dtos;
    }

    public User findStudent(Long id) {
        return whoCanSee.getSearchedUser(id, Role.ROLE_STUDENT, "Student");
    }

}
