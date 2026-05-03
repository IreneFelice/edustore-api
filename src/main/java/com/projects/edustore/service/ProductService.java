package com.projects.edustore.service;

import com.projects.edustore.dto.product.*;
import com.projects.edustore.exception.ForbiddenActionException;
import com.projects.edustore.exception.ResourceNotFoundException;
import com.projects.edustore.mapper.ProductMapper;
import com.projects.edustore.model.Role;
import com.projects.edustore.model.User;
import com.projects.edustore.model.person.StudentProfile;
import com.projects.edustore.model.product.Product;
import com.projects.edustore.repository.ProductRepository;

import com.projects.edustore.repository.UserRepository;
import com.projects.edustore.security.AuthorisationService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ProductService {
    private final ProductRepository productRepos;
    private final UserRepository userRepos;
    private final AuthorisationService authorizer;

    public ProductService(ProductRepository productRepos, UserRepository userRepos, AuthorisationService authorizer) {
        this.productRepos = productRepos;
        this.userRepos = userRepos;
        this.authorizer = authorizer;
    }

    public List<ProductCustomerResponseDto> getAllProductsForCustomer() {
        List<Product> products = productRepos.findAll();
        List<ProductCustomerResponseDto> dtos = new ArrayList<>();
        for (Product product : products) {
            dtos.add(ProductMapper.toCustomerResponseDto(product));
        }
        return dtos;
    }

    public TeamNamesResponseDto getAllUniqueTeams() {
        List<String> teamNames = userRepos.findAllUniqueTeams();
        return new TeamNamesResponseDto(teamNames);
    }

    public List<ProductCustomerResponseDto> getProductsByTeam(String team) {
        List<Product> products = productRepos.findByMaker_TeamIgnoreCase(team);

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
        List<Product> products = productRepos.findByMaker_Id(studentId);
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
            throw new ForbiddenActionException();
        }
        return ProductMapper.toStudentResponseDto(product);
    }

    @Transactional
    public ProductStudentResponseDto createNewProduct(ProductRequestDto dto, Long studentId) {
        User user = findStudentUserAndCheckAuthorisation(studentId);
        Product newProduct = ProductMapper.toEntity(dto, user.getPerson().getStudentProfile());
        productRepos.save(newProduct);
        return ProductMapper.toStudentResponseDto(newProduct);
    }

    @Transactional
    public ProductStudentResponseDto updateProductByMaker(Long studentId, Long productId, ProductUpdateDto dto) {
        authorizeStudentAccess(studentId);
        Product existingProduct = findProduct(productId);
        checkStudentIsMaker(studentId, existingProduct);

        ProductMapper.updateProduct(existingProduct, dto);
        productRepos.save(existingProduct);
        return ProductMapper.toStudentResponseDto(existingProduct);
    }

    @Transactional
    public void deleteProductByMaker(Long studentId, Long productId) {
        authorizeStudentAccess(studentId);
        Product product = findProduct(productId);
        checkStudentIsMaker(studentId, product);
        productRepos.delete(product);
    }

    @Transactional
    public void updateMaker(Long studentId, Long productId, UpdateProductMakerDto dto) {
        authorizeStudentAccess(studentId);
        Product product = findProduct(productId);
        checkStudentIsMaker(studentId, product);

        User newMaker = userRepos.findById(dto.getNewMakerId())
                .orElseThrow(() -> new ResourceNotFoundException("Maker", dto.getNewMakerId()));

        if (newMaker.getPerson().getStudentProfile() != null) {
            product.setMaker(newMaker.getPerson().getStudentProfile());
            productRepos.save(product);
        } else {
            throw new ResourceNotFoundException("Maker", dto.getNewMakerId());
        }
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

    @Transactional
    public void uploadProductImageByMaker(Long studentId, Long productId, MultipartFile file) throws IOException {
        authorizeStudentAccess(studentId);
        Product product = findProduct(productId);

        checkStudentIsMaker(studentId, product);
        uploadProductImage(product, file);
    }

    @Transactional
    public void deleteProductImageByMaker(Long studentId, Long productId) {
        authorizeStudentAccess(studentId);
        Product product = findProduct(productId);
        checkStudentIsMaker(studentId, product);

        product.removeImage();
        productRepos.save(product);
    }

    // helpers
    private void uploadProductImage(Product product, MultipartFile file) throws IOException {

        if (!ALLOWED_IMAGE_TYPES.contains(file.getContentType()) || file.getSize() > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("Max 1MB and only jpeg, png or webp image types allowed.");
        }
        product.addImage(
                file.getBytes(),
                file.getContentType(),
                file.getOriginalFilename()
        );
        productRepos.save(product);
    }

    private void authorizeStudentAccess(Long id) {
        authorizer.checkSelfOrAdminAccess(id);
    }

    private User findStudentUserAndCheckAuthorisation(Long id) {
        return authorizer.findUserAndCheckAuthorisation(id).orElseThrow(() -> new ResourceNotFoundException("Student", id));
    }

    private Product findProduct(Long productId) {
        return productRepos.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", productId));
    }

    private void checkStudentIsMaker(Long studentId, Product product) {
        User currentUser = authorizer.getCurrentUser();
        StudentProfile maker = product.getMaker();

        boolean isMaker = studentId.equals(maker.getId());
        boolean isAdmin = currentUser.getRole().equals(Role.ROLE_ADMIN);

        if (!isMaker && !isAdmin) {
            throw new ForbiddenActionException();
        }
    }

}
