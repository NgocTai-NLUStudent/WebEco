package com.example.mobileapi.graphql.query;

import com.example.mobileapi.dto.response.ProductResponseDTO;
import com.example.mobileapi.entity.Product;
import com.example.mobileapi.exception.AppException;
import com.example.mobileapi.mapper.ProductMapper;
import com.example.mobileapi.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductQueryResolver {

    ProductService productService;
    ProductMapper productMapper;

    @QueryMapping
    public List<Product> products() {
        return productMapper.toProductList(productService.getAllProducts());
    }

    @QueryMapping
    public Product productById(@Argument Integer id) throws AppException {
        return productMapper.toProduct(productService.getProductById(id));
    }

    @QueryMapping
    public List<Product> productsByName(@Argument String name) {
        return productMapper.toProductList(productService.getProductByName(name));
    }

    @QueryMapping
    public List<Product> productsByCategoryId(@Argument Integer categoryId) {
        return productMapper.toProductList(productService.findByCategoryId(categoryId));
    }
}
