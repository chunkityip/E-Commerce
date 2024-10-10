package com.example.E_Commerce.service.impl;

import com.example.E_Commerce.dto.ProductDto;
import com.example.E_Commerce.dto.Response;
import com.example.E_Commerce.entity.Category;
import com.example.E_Commerce.entity.Product;
import com.example.E_Commerce.exception.NotFoundException;
import com.example.E_Commerce.mapper.EntityDtoMapper;
import com.example.E_Commerce.repository.CategoryRepo;
import com.example.E_Commerce.repository.ProductRepo;
import com.example.E_Commerce.service.AwsS3Service;
import com.example.E_Commerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final EntityDtoMapper entityDtoMapper;
    private final AwsS3Service awsS3Service;

    @Override
    public Response createProduct(Long categoryId, MultipartFile image, String name, String description, BigDecimal price) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(
                () -> new NotFoundException("Category not found"));

        String productImageUrl = awsS3Service.saveImageToS3(image);

        Product product = new Product();
        product.setCategory(category);
        product.setPrice(price);
        product.setName(name);
        product.setDescription(description);
        product.setImageUrl(productImageUrl);

        productRepo.save(product);
        return Response.builder()
                .status(200)
                .message("Product successfully created")
                .build();
    }

    @Override
    public Response updateProduct(Long productId, Long categoryId, MultipartFile image, String name, String description, BigDecimal price) {
        return null;
    }

    @Override
    public Response deleteProduct(Long productId) {
        return null;
    }

    @Override
    public Response getProductById(Long productId) {
        return null;
    }

    @Override
    public Response getAllProducts() {
        return null;
    }

    @Override
    public Response getProductsByCategory(Long productId) {
        return null;
    }

    @Override
    public Response searchProduct(String searchValue) {
        return null;
    }
}
