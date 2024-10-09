package com.example.E_Commerce.service.impl;

import com.example.E_Commerce.dto.CategoryDto;
import com.example.E_Commerce.dto.Response;
import com.example.E_Commerce.entity.Category;
import com.example.E_Commerce.repository.CategoryRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepo categoryRepo;

    @InjectMocks
    private CategoryServiceImpl categoryService;

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
    void updateCategory() {
    }

    @Test
    void getAllCategories() {
    }

    @Test
    void getCategoryById() {
    }

    @Test
    void deleteCategory() {
    }
}