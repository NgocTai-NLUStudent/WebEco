package com.example.mobileapi.controller;

import com.example.mobileapi.dto.request.CategoryRequestDTO;
import com.example.mobileapi.dto.response.CategoryResponseDTO;
import com.example.mobileapi.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
@Tag(name = "Category", description = "Category API")
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class CategoryController {

    CategoryService categoryService;

    @PostMapping
    public int addCategory(@RequestBody CategoryRequestDTO category) {
        return categoryService.saveCategory(category);
    }

    @PutMapping("/{categoryId}")
    public void updateCategory(@PathVariable int categoryId, @RequestBody CategoryRequestDTO category) {
        categoryService.updateCategory(categoryId, category);
    }

    @DeleteMapping("/{categoryId}")
    public void deleteCategory(@PathVariable int categoryId) {
        categoryService.deleteCategory(categoryId);
    }

    @GetMapping("/list")
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{categoryId}")
    public CategoryResponseDTO getCategory(@PathVariable int categoryId) {
        return categoryService.getCategory(categoryId);
    }
}
