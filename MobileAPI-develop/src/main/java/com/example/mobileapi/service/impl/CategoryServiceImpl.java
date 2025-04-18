package com.example.mobileapi.service.impl;

import com.example.mobileapi.dto.request.CategoryRequestDTO;
import com.example.mobileapi.dto.response.CategoryResponseDTO;
import com.example.mobileapi.exception.AppException;
import com.example.mobileapi.exception.ErrorCode;
import com.example.mobileapi.mapper.CategoryMapper;
import com.example.mobileapi.entity.Category;
import com.example.mobileapi.repository.CategoryRepository;
import com.example.mobileapi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    @Override
    public CategoryResponseDTO saveCategory(CategoryRequestDTO dto) {
        Category category = categoryMapper.toCategory(dto);
        return categoryMapper.toCategoryResponseDTO(category);
    }

    @Override
    public CategoryResponseDTO updateCategory(int id, CategoryRequestDTO dto) throws AppException {
        if (!categoryRepository.existsById(dto.getId())) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);

        }
        Category entity = categoryMapper.toCategory(dto);
        entity.setId(id);
        return categoryMapper.toCategoryResponseDTO(
                categoryRepository.save(entity));
    }

    @Override
    public void deleteCategory(int id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryResponseDTO getCategory(int id) throws AppException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .img(category.getImg())
                .build();
    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponseDTO> categoriesResponseDTO = new ArrayList<>();
        for (Category category : categories) {
            categoriesResponseDTO.add(CategoryResponseDTO.builder()
                    .name(category.getName())
                    .img(category.getImg())
                    .id(category.getId())
                    .build());
        }
        return categoriesResponseDTO;
    }

    @Override
    public CategoryResponseDTO getCategoryById(int categoryId) throws AppException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        return categoryMapper.toCategoryResponseDTO(category);
    }

    @Override
    public Category getCategoryByName(String name) throws AppException {
        return
                categoryRepository.findByName(name)
                        .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }


}
