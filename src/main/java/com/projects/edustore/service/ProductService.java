package com.projects.edustore.service;

import com.projects.edustore.dto.productDto.ProductCustomerResponseDto;
import com.projects.edustore.dto.productDto.ProductRequestDto;
import com.projects.edustore.dto.productDto.ProductStudentResponseDto;
import com.projects.edustore.exception.ForbiddenActionException;
import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.mapper.ProductMapper;
import com.projects.edustore.model.Role;
import com.projects.edustore.model.User;
import com.projects.edustore.model.person.StudentProfile;
import com.projects.edustore.model.products.Product;
import com.projects.edustore.repository.ProductRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ProductService {
    private final ProductRepository repos;
    private final WhoCanSeeWhoService whoCanSee;

    public ProductService(ProductRepository repos, WhoCanSeeWhoService whoCanSee) {
        this.repos = repos;
        this.whoCanSee = whoCanSee;
    }

    public List<ProductCustomerResponseDto> getAllProductsForCustomer() {
        List<Product> products = repos.findAll();
        List<ProductCustomerResponseDto> dtos = new ArrayList<>();
        for (Product product : products) {
            dtos.add(ProductMapper.toCustomerResponseDto(product));
        }
        return dtos;
    }

    public List<ProductCustomerResponseDto> getProductsBySchoolPeriod(String schoolPeriod) {
        List<Product> products = repos.findByMaker_SchoolPeriodIgnoreCase(schoolPeriod);

        List<ProductCustomerResponseDto> dtos = new ArrayList<>();
        for (Product product : products) {
            dtos.add(ProductMapper.toCustomerResponseDto(product));
        }
        return dtos;
    }

    public ProductCustomerResponseDto getProductForCustomer(Long productId) {
        Product product = repos.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", productId));
        return ProductMapper.toCustomerResponseDto(product);
    }

    //
//    public OrderCustomerResponseDto createOrderFromCart(OrderCustomerRequestDto orderCustomerRequestDto) {
//return OrderCustomerResponseDto;

    /////////////////////////FOR STUDENTS/////////////////////////////////////////////////


    public List<ProductStudentResponseDto> getAllProductsByMaker(Long studentId) {
        authorizeStudentAccess(studentId);
        List<Product> products = repos.findByMaker_Id(studentId);
        List<ProductStudentResponseDto> dtos = new ArrayList<>();

        for (Product product : products) {
            dtos.add(ProductMapper.toStudentResponseDto(product));
        }
        return dtos;
    }


    public ProductStudentResponseDto getProductForStudent(Long studentId, Long productId) {
        authorizeStudentAccess(studentId);
        Product product = repos.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", productId));
        return ProductMapper.toStudentResponseDto(product);
    }


    public ProductStudentResponseDto createNewProduct(ProductRequestDto dto, Long studentId) {
        User user = whoCanSee.findUserAndCheckPermission(studentId, Role.ROLE_STUDENT, "Student");
        Product newProduct = ProductMapper.toEntity(dto, user.getPerson().getStudentProfile());
        repos.save(newProduct);
        return ProductMapper.toStudentResponseDto(newProduct);
    }


    public ProductStudentResponseDto updateProduct(Long studentId, Long productId, ProductRequestDto dto) {
        authorizeStudentAccess(studentId);
        Product existingProduct = repos.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", productId));
        ProductMapper.updateProduct(existingProduct, dto);
        repos.save(existingProduct);
        return ProductMapper.toStudentResponseDto(existingProduct);
    }

    //TODO delete product

    ////// Product image

    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            "image/webp"
    );

    private static final long MAX_IMAGE_SIZE = 1_000_000; // 1 MB

    public Product getProductForImage(Long productId) {
        Product product = repos.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", productId));
        if (product.getBytes() == null) {
            throw new ResourceNotFoundException("Image for product ", productId);
        }
        return product;
    }


    public void uploadProductImage(Long productId, MultipartFile file) throws IOException {
        Product product = repos.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        if (!ALLOWED_IMAGE_TYPES.contains(file.getContentType()) || file.getSize() > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("Max 1MB and only jpeg, png or webp image types allowed.");
        }
        product.addImage(file.getBytes(), file.getContentType(), file.getOriginalFilename());
        repos.save(product);
    }


    public void uploadProductImageByMaker(Long studentId, Long productId, MultipartFile file) throws IOException {
        Product product = repos.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", productId));
        authorizeStudentAccess(studentId);

        StudentProfile maker = product.getMaker();
        User currentUser = whoCanSee.getCurrentUser();

        boolean isMaker = studentId.equals(maker.getId());
        boolean isAdmin = currentUser.getRole().equals(Role.ROLE_ADMIN);

        if (!isMaker && !isAdmin) {
            throw new ForbiddenActionException("No permission to upload. This is not your item.");
        }
        uploadProductImage(productId, file);
    }

    //TODO delete image

    public void authorizeStudentAccess(Long id) {
        whoCanSee.checkUserPermission(id, Role.ROLE_STUDENT, "Student"); //current User is allowed and requested user (by admin) or current user is Student
    }

}
