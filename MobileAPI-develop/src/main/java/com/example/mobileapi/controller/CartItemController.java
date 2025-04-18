package com.example.mobileapi.controller;

import com.example.mobileapi.dto.request.CartItemRequestDTO;
import com.example.mobileapi.dto.response.ApiResponse;
import com.example.mobileapi.service.CartItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cartItem")
@RequiredArgsConstructor
@Tag(name = "CartItem", description = "CartItem API")
public class CartItemController {
    private final CartItemService cartItemService;

    @PostMapping
    public ApiResponse<Void> saveCartItem(@RequestBody CartItemRequestDTO cartItemRequestDTO) {
        cartItemService.saveCartItem(cartItemRequestDTO);
        return ApiResponse.success();
    }

    @PutMapping("/updatequantity/{cartItemId}")
    public ApiResponse<Void> updateCartItem(@PathVariable("cartItemId") int cartItemId, @RequestParam int quantity) {
        cartItemService.updateCartItemQuantity(cartItemId, quantity);
        return ApiResponse.success();
    }

    @DeleteMapping("/{cartItemId}")
    public ApiResponse<Void> deleteCartItem(@PathVariable("cartItemId") int cartItemId) {
        cartItemService.deleteCartItem(cartItemId);

        return ApiResponse.success();
    }

    @DeleteMapping("/cartId/{cartId}")
    public ApiResponse<Void> deleteCartItemByCartId(@PathVariable("cartId") int cartId) {
        cartItemService.deleteCartItemByCartId(cartId);
        return ApiResponse.success();
    }
}
