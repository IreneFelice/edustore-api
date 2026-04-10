package com.projects.edustore.service;

import com.projects.edustore.dto.product.ProductCustomerResponseDto;
import com.projects.edustore.dto.product.ProductRequestDto;
import com.projects.edustore.dto.product.ProductStudentResponseDto;
import com.projects.edustore.exception.ForbiddenActionException;
import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.mapper.ProductMapper;
import com.projects.edustore.model.Role;
import com.projects.edustore.model.User;
import com.projects.edustore.model.person.StudentProfile;
import com.projects.edustore.model.product.Product;
import com.projects.edustore.repository.ProductRepository;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository repos;
    private final AuthorisationService whoCanSee;

    public ProductService(ProductRepository repos, AuthorisationService whoCanSee) {
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

    public List<ProductCustomerResponseDto> getProductsByTeam(String team) {
        List<Product> products = repos.findByMaker_TeamIgnoreCase(team);

        List<ProductCustomerResponseDto> dtos = new ArrayList<>();
        for (Product product : products) {
            dtos.add(ProductMapper.toCustomerResponseDto(product));
        }
        return dtos;
    }

    public ProductCustomerResponseDto getProductForCustomer(Long productId) {
        return ProductMapper.toCustomerResponseDto(findProduct(productId));
    }


    /////////////////////////FOR STUDENTS/////////////////////////////////////////////////


    public List<ProductStudentResponseDto> getAllProductsForMaker(Long studentId) {
        authorizeStudentAccess(studentId);
        List<Product> products = repos.findByMaker_Id(studentId);
        List<ProductStudentResponseDto> dtos = new ArrayList<>();

        for (Product product : products) {
            dtos.add(ProductMapper.toStudentResponseDto(product));
        }
        return dtos;
    }

    public ProductStudentResponseDto getProductDetailsForTeam(Long studentId, Long productId) {
        User user = findStudentUserAndCheckAuthorisation(studentId);
        Product product = findProduct(productId);

        String userTeam = user.getPerson().getStudentProfile().getTeam();
        String productMakerTeam = product.getMaker().getTeam();

        if (!userTeam.equals(productMakerTeam)) {
            throw new ForbiddenActionException("Protected details of this product can not be accessed, because it's not owned by your team.");
        }
        return ProductMapper.toStudentResponseDto(product);
    }

    @Transactional(readOnly = false)
    public ProductStudentResponseDto createNewProduct(ProductRequestDto dto, Long studentId) {
        User user = findStudentUserAndCheckAuthorisation(studentId);
        Product newProduct = ProductMapper.toEntity(dto,user.getPerson().getStudentProfile());
        repos.save(newProduct);
        return ProductMapper.toStudentResponseDto(newProduct);
    }

    @Transactional(readOnly = false)
    public ProductStudentResponseDto updateProductByMaker(Long studentId, Long productId, ProductRequestDto dto) {
        authorizeStudentAccess(studentId);
        Product existingProduct = findProduct(productId);
        checkStudentIsMaker(studentId, existingProduct);

        ProductMapper.updateProduct(existingProduct, dto);
        repos.save(existingProduct);
        return ProductMapper.toStudentResponseDto(existingProduct);
    }

    @Transactional(readOnly = false)
    public void deleteProductByMaker(Long studentId, Long productId) {
        authorizeStudentAccess(studentId);
        Product product = findProduct(productId);
        checkStudentIsMaker(studentId, product);
        repos.delete(product);
    }


    ////// Product image

    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            "image/webp"
    );

    private static final long MAX_IMAGE_SIZE = 1_000_000; // 1 MB

    public Product getProductWithImage(Long productId) {
        Product product = findProduct(productId);
        if (product.getBytes() == null) {
            throw new ResourceNotFoundException("Image for product", productId);
        }
        return product;
    }


    @Transactional(readOnly = false)
    public void uploadProductImageByMaker(Long studentId, Long productId, MultipartFile file) throws IOException {
        authorizeStudentAccess(studentId);
        Product product = findProduct(productId);

        checkStudentIsMaker(studentId, product);
        uploadProductImage(product, file);
    }

    @Transactional(readOnly = false)
    public void deleteProductImageByMaker(Long studentId, Long productId) {
        authorizeStudentAccess(studentId);
        Product product = findProduct(productId);
        checkStudentIsMaker(studentId, product);

        product.removeImage();
        repos.save(product);
    }


    private void uploadProductImage(Product product, MultipartFile file) throws IOException {

        if (!ALLOWED_IMAGE_TYPES.contains(file.getContentType()) || file.getSize() > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("Max 1MB and only jpeg, png or webp image types allowed.");
        }
        product.addImage(
                file.getBytes(),
                file.getContentType(),
                file.getOriginalFilename()
        );
        repos.save(product);
    }

    //helpers

    public void authorizeStudentAccess(Long id) {
        whoCanSee.checkSelfOrAdminAccess(id);
    }

    public User findStudentUserAndCheckAuthorisation(Long id) {
        return whoCanSee.findUserAndCheckAuthorisation(id).orElseThrow(() -> new ResourceNotFoundException("Student", id));
    }

    public Product findProduct(Long productId) {
        return repos.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", productId));
    }

    public void checkStudentIsMaker(Long studentId, Product product) {
        User currentUser = whoCanSee.getCurrentUser();
        StudentProfile maker = product.getMaker();

        boolean isMaker = studentId.equals(maker.getId());
        boolean isAdmin = currentUser.getRole().equals(Role.ROLE_ADMIN);

        if (!isMaker && !isAdmin) {
            throw new ForbiddenActionException("No permission for requested action. This is not your item.");
        }
    }

}
