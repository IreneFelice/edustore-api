package com.projects.edustore.service;

import com.projects.edustore.dto.productDto.ProductCustomerResponseDto;
import com.projects.edustore.dto.productDto.ProductRequestDto;
import com.projects.edustore.dto.productDto.ProductStudentResponseDto;
import com.projects.edustore.exception.ForbiddenActionException;
import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.mapper.ProductMapper;
import com.projects.edustore.model.Role;
import com.projects.edustore.model.User;
import com.projects.edustore.model.products.Product;
import com.projects.edustore.repository.ProductRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductService {
    private final ProductRepository repos;
    private final WhoCanSeeWhoService whoCanSee;

    public ProductService(ProductRepository repos, WhoCanSeeWhoService whoCanSee) {
        this.repos = repos;
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

    public Optional<Product> getProductById(Long productId) {
        Optional<Product> product = repos.findById(productId);
        return product;
    }

    public List<ProductCustomerResponseDto> getProductsBySchoolPeriod(String schoolPeriod) {
        List<Product> products = repos.findByMaker_SchoolPeriod(schoolPeriod);
        List<ProductCustomerResponseDto> dtos = new ArrayList<>();
        for (Product product : products) {
            dtos.add(ProductMapper.toCustomerResponseDto(product));
        }
        return dtos;
    }


//
//    public OrderCustomerResponseDto createOrderFromCart(OrderCustomerRequestDto orderCustomerRequestDto) {
//return OrderCustomerResponseDto;


    public ProductStudentResponseDto createNewProduct(ProductRequestDto dto, Long id) {
        User user = authorizeStudentAccess(id); //current User is allowed, requested user is Student
        Product newProduct = ProductMapper.toEntity(dto, user.getPerson().getStudentProfile());

        repos.save(newProduct);
        return ProductMapper.toStudentResponseDto(newProduct);
    }


    public List<ProductStudentResponseDto> getAllProductsByMaker(Long id) {
        authorizeStudentAccess(id);
        List<Product> products = repos.findByMakerId(id);
        List<ProductStudentResponseDto> dtos = new ArrayList<>();

        for (Product product : products) {
            dtos.add(ProductMapper.toStudentResponseDto(product));
        }
        return dtos;
    }

    ////// Product image //////////////

    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            "image/webp"
    );

    private static final long MAX_IMAGE_SIZE = 1_000_000; // 1 MB


    public void uploadProductImage(Long productId, MultipartFile file) throws IOException {
        Product product = repos.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        if (!ALLOWED_IMAGE_TYPES.contains(file.getContentType()) ||  file.getSize()>MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("Max 1MB and only jpeg, png or webp image types allowed.");
        }
        product.addImage(file.getBytes(), file.getContentType(), file.getOriginalFilename());
        repos.save(product);
    }

    public void uploadProductImageByMaker(Long studentId, Long productId, MultipartFile file) throws IOException {
        Product product = repos.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", productId));
        authorizeStudentAccess(studentId);
        if (!studentId.equals(product.getMaker().getId())) {
            throw new ForbiddenActionException("No permission to upload. This is not your item.");
        }
        uploadProductImage(productId, file);
    }

    public User authorizeStudentAccess(Long id) {
        return whoCanSee.authorizeUserAccess(id, Role.ROLE_STUDENT, "Student");
    }


}
