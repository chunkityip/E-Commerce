package com.example.E_Commerce.service.impl;

import com.example.E_Commerce.dto.CategoryDto;
import com.example.E_Commerce.dto.Response;
import com.example.E_Commerce.entity.Category;
import com.example.E_Commerce.exception.NotFoundException;
import com.example.E_Commerce.mapper.EntityDtoMapper;
import com.example.E_Commerce.repository.CategoryRepo;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepo categoryRepo;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private EntityDtoMapper entityDtoMapper;

    private Category category;
    private CategoryDto categoryDto;

    @Test
    void createCategoryTest() {
        categoryDto = new CategoryDto();
        String name = "Hello";
        categoryDto.setName(name);

        category = new Category();
        category.setName(categoryDto.getName());

        when(categoryRepo.save(any(Category.class))).thenReturn(category);

        Response response = categoryService.createCategory(categoryDto);

        assertAll(
                () -> assertEquals(200 , response.getStatus() , "Status should match"),
                () -> assertEquals("Category created successfully" , response.getMessage()
                , "Message should match")
        );

        verify(categoryRepo, times(1)).save(argThat(cat ->
                cat.getName().equals(name)
        ));
    }

    @Test
    void categoryNotFoundExceptionTest() {
        Long categoryId = null;
        categoryDto = new CategoryDto();

        when(categoryRepo.findById(isNull()))
                .thenThrow(new NotFoundException("Category not found"));

        assertThrows(NotFoundException.class, () ->
                categoryService.updateCategory(categoryId , categoryDto));

        verify(categoryRepo , times(1)).findById(null);
    }


    @Test
    void updateCategorySuccessfulTest() {
        // Arrange
        Long categoryId = 1L;
        categoryDto = new CategoryDto();
        categoryDto.setName("Iphone");

        category = new Category();
        category.setName("phone");

        // Stub
        when(categoryRepo.findById(categoryId)).thenReturn(Optional.of(category));

        // Act
        Response response = categoryService.updateCategory(categoryId , categoryDto);

        assertAll(
                () -> assertEquals(200 , response.getStatus() , "Status should match"),
                () -> assertEquals("Category updated successfully" , response.getMessage(),
                        "Message should match")
        );

        verify(categoryRepo , times(1)).findById(categoryId);
        verify(categoryRepo , times(1)).save(category);

        assertEquals("Iphone" , category.getName() ,
                "The category name should be updated to 'Iphone'");
    }

    @Test
    void getAllCategoriesSuccessfullWithTwoCategory() {
        // Arrange
        category = new Category();
        Category category2 = new Category();

        category.setName("CK");
        category2.setName("Lawrence");

        List<Category> categoryDtoList = List.of(category , category2);

        // Stub
        when(categoryRepo.findAll()).thenReturn(categoryDtoList);
        when(entityDtoMapper.mapCategoryToDtoBasic(category)).thenReturn(categoryDto);

        // Act
        Response response = categoryService.getAllCategories();

        // Assert
        assertAll(
                () -> assertEquals(200 , response.getStatus() , "Status should match"),
                () -> assertEquals(2 , categoryDtoList.size() ,
                        "The size should match since there has two Category")
        );
    }

    @Test
    void getCategoryById() {
    }

    @Test
    void deleteCategory() {
    }
}