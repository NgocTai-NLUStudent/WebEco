package com.example.mobileapi.graphql.mutation;

import com.example.mobileapi.dto.request.CategoryRequestDTO;
import com.example.mobileapi.dto.response.CategoryResponseDTO;
import com.example.mobileapi.exception.AppException;
import com.example.mobileapi.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
public class CategoryMutationResolver {

    CategoryService categoryService;

    @MutationMapping
    public CategoryResponseDTO addCategory(@Argument("input") CategoryRequestDTO category) {
        return categoryService.saveCategory(category);
    }

    @MutationMapping
    public CategoryResponseDTO updateCategory(@Argument("id") int id, @Valid @Argument("input") CategoryRequestDTO category) throws AppException {
        return categoryService.updateCategory(id, category);

    }

    @MutationMapping
    public Boolean deleteCategory(@Argument int id) {
        categoryService.deleteCategory(id);
        return Boolean.TRUE;
    }
}
