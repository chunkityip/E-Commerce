package com.example.E_Commerce.service;

import com.example.E_Commerce.dto.CategoryDto;
import com.example.E_Commerce.dto.Response;

public interface CategoryService {

    Response createCategory(CategoryDto categoryDto);

    Response updateCategory(Long categoryId, CategoryDto categoryDto);

    Response getAllCategories();

    Response getCategoryById(Long categoryId);

    Response deleteCategory(Long categoryId);
}
