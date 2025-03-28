package com.example.mobileapi.controller;

import com.example.mobileapi.dto.response.ApiResponse;
import com.example.mobileapi.dto.response.UrlResponse;
import com.example.mobileapi.service.ImageUploadService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/cloudinary")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Tag(name = "ImageUpload", description = "Image Upload API")
public class ImageUploadController {
    ImageUploadService imageUploadService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<UrlResponse> uploadImage(@RequestParam("file") MultipartFile file) {

        return ApiResponse.<UrlResponse>builder()
                .code(200)
                .data(imageUploadService.uploadImage(file))
                .build();
    }


}
