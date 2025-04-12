package com.example.mobileapi.controller;

import com.example.mobileapi.annotation.GetToken;
import com.example.mobileapi.dto.request.*;
import com.example.mobileapi.dto.response.ApiResponse;
import com.example.mobileapi.dto.response.CustomerResponseDTO;
import com.example.mobileapi.exception.AppException;
import com.example.mobileapi.exception.ErrorCode;
import com.example.mobileapi.service.CartService;
import com.example.mobileapi.service.CustomerService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@Tag(name = "Customer", description = "Customer API")
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@PreAuthorize("hasRole('USER')")
public class CustomerController {
    CustomerService customerService;
    CartService cartService;

    @GetMapping("/quantity/{customerId}")
    public ApiResponse<Integer> getQuantity(@PathVariable("customerId") int customerId) {
        return ApiResponse.<Integer>builder().data(customerService.getQuantityByCustomerId(customerId)).build();

    }


    @PutMapping("/{customerId}")
    public ApiResponse<Void> updateCustomer(@PathVariable int customerId, @RequestBody CustomerRequestDTO customer) {
        try {
            customerService.updateCustomerById(customerId, customer);
            return ApiResponse.success("Cập nhật thông tin người dùng thành công");
        } catch (AppException e) {
            throw new RuntimeException(e);
        }

    }


    @GetMapping("/profile")
    public ApiResponse<CustomerResponseDTO> getCustomerFromToken(@Parameter(hidden = true) @GetToken String token) {


        try {
            return ApiResponse.<CustomerResponseDTO>builder()
                    .data(customerService.getCustomerProfile(token))
                    .build();
        } catch (AppException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/checkUsername/{username}")
    public ApiResponse<Boolean> checkUsername(@PathVariable String username) {

        return ApiResponse.<Boolean>builder().data(customerService.checkUsername(username)).build();
    }


    @PostMapping("/resetPassword/{username}")
    public ApiResponse<Void> resetPassword(
            @PathVariable String username,
            @RequestParam String resetCode,
            @RequestParam String newPassword) {
        try {
            customerService.resetPassword(username, resetCode, newPassword);
        } catch (AppException e) {
            throw new RuntimeException(e);
        }

        return ApiResponse.success("Đặt lại mật khẩu thành công");

    }


    @PostMapping("/initPasswordReset/{username}")
    public ApiResponse<Void> initPasswordReset(@PathVariable String username) {
        try {
            customerService.initPasswordReset(username);
        } catch (AppException e) {
            throw new RuntimeException(e);
        }

        return ApiResponse.success("Gửi mã xác nhận thành công");
    }


    @PatchMapping("/changePassword/{customerId}")
    public ApiResponse<Void> changePassword(@PathVariable int customerId, @RequestBody ChangePasswordDto dto) throws AppException {
        customerService.changePassword(customerId, dto.getOldPassword(), dto.getNewPassword());

        return ApiResponse.success("Đổi mật khẩu thành công");
    }

}

