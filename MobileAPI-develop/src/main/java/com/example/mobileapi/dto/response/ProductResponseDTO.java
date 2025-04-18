package com.example.mobileapi.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
public class ProductResponseDTO {
    Integer id;
    String name;
    String img;
    int price;
    String detail;
    String supplier;
    String author;
    Integer publishYear;
    String publisher;
    String language;
    Byte weight;
    String size;
    Integer pageNumber;
    String form;
    Integer categoryId;
    String categoryName;
}
