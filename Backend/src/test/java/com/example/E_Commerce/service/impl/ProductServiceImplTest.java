package com.example.E_Commerce.service.impl;

import com.example.E_Commerce.ECommerceApplication;
import com.example.E_Commerce.dto.Response;
import com.example.E_Commerce.entity.Category;
import com.example.E_Commerce.entity.Product;
import com.example.E_Commerce.exception.NotFoundException;
import com.example.E_Commerce.mapper.EntityDtoMapper;
import com.example.E_Commerce.repository.CategoryRepo;
import com.example.E_Commerce.repository.ProductRepo;
import com.example.E_Commerce.service.AwsS3Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepo productRepo;

    @Mock
    private CategoryRepo categoryRepo;

    @Mock
    private AwsS3Service awsS3Service;

    @Mock
    private MultipartFile image;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    @DisplayName("Should successfully create a product")
    void shouldCreateProductSuccessfully() {
        // Arrange
        Long categoryId = 1L;
        String name = "Test Product";
        String description = "Test Description";
        BigDecimal price = new BigDecimal("100.00");

        image = mock(MultipartFile.class);

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Electronics");

        String productImageUrl = "https://s3.amazonaws.com/test/product-image.jpg";

        //
        when(categoryRepo.findById(categoryId)).thenReturn(Optional.of(category));
        when(awsS3Service.saveImageToS3(image)).thenReturn(productImageUrl);

        // Act
        Response response = productService.createProduct(categoryId, image, name, description, price);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus(), "Status should be 200"),
                () -> assertEquals("Product successfully created", response.getMessage(), "Message should match")
        );

        // Verify
        verify(categoryRepo, times(1)).findById(categoryId);
        verify(awsS3Service, times(1)).saveImageToS3(image);
        verify(productRepo, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Should throw NotFoundException when category is not found")
    void shouldThrowNotFoundExceptionWhenCategoryNotFound() {
        // Arrange
        Long inCorrectId = 1L;
        String name = "Test Product";
        String description = "Test Description";
        BigDecimal price = new BigDecimal("100.00");

        MultipartFile image = mock(MultipartFile.class);

        // Stub
        when(categoryRepo.findById(inCorrectId))
                .thenThrow(new NotFoundException("Category not found"));

        // Act
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> productService.createProduct(inCorrectId, image, name, description, price));

        // Assert
        assertEquals("Category not found", exception.getMessage(), "The exception message should match");

        // Verify
        verify(awsS3Service, never()).saveImageToS3(any());
        verify(productRepo, never()).save(any());
        verify(categoryRepo , times(1)).findById(inCorrectId);
    }
}