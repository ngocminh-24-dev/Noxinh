package org.company.noxinh.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.company.noxinh.constants.ResponseCode;
import org.company.noxinh.dto.ResponseData;
import org.company.noxinh.dto.cart.request.UpdateCartRequest;
import org.company.noxinh.dto.cart.response.GetCartDetailResponse;
import org.company.noxinh.dto.cart.response.GetCartResponse;
import org.company.noxinh.service.CartService;
import org.company.noxinh.utils.ServiceUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer")
public class CartController {
    private final CartService cartService;

    @GetMapping("/")
    public ResponseEntity<ResponseData<GetCartResponse>> getCartByUser(Pageable pageable) {
        Integer accountId = ServiceUtils.getId();
        ResponseData<GetCartResponse> response = cartService.getProductInCartByAccountId(accountId, pageable);
        return getResponse(response);
    }

    @GetMapping("/detail/{productId}")
    public ResponseEntity<ResponseData<GetCartDetailResponse>> getCartByUserIdAndProductId(@PathVariable Integer productId) {
        Integer accountId = ServiceUtils.getId();
        ResponseData<GetCartDetailResponse> response = cartService.getProductInCartByAccountIdAndProductId(accountId, productId);
        return getResponse(response);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<ResponseData<GetCartResponse>> getCartByAccountId(@PathVariable Integer accountId, Pageable pageable) {
        ResponseData<GetCartResponse> response = cartService.getProductInCartByAccountId(accountId, pageable);
        return getResponse(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ResponseData<String>> removeFromCart(@PathVariable Integer productId) {
        ResponseData<String> response = cartService.removeProductFromCart(ServiceUtils.getId(), productId);
        return getResponse(response);
    }

    @PutMapping("/")
    public ResponseEntity<ResponseData<String>> updateAmount(@RequestBody UpdateCartRequest request){
        if (request.getAmount() == null || request.getAmount() <= 0)
            return new ResponseEntity<>(ResponseData.badRequest("Số lượng không được để trống và phải lớn hơn 0."), HttpStatusCode.valueOf(ResponseCode.C005.getCode()));
        ResponseData<String> response = cartService.updateAmount(request);
        return getResponse(response);
    }

    @PostMapping("/{productId}")
    public ResponseEntity<ResponseData<String>> addToCart(@PathVariable Integer productId, @RequestParam Integer amount) {
        if (amount == null || amount <= 0)
            return new ResponseEntity<>(ResponseData.badRequest("Số lượng không được để trống và phải lớn hơn 0."), HttpStatusCode.valueOf(ResponseCode.C005.getCode()));
        ResponseData<String> response = cartService.addProductToCart(ServiceUtils.getId(), productId, amount);
        return getResponse(response);
    }

    protected <T> ResponseEntity<ResponseData<T>> getResponse(ResponseData<T> response) {
        if (response.getStatus() == ResponseCode.C001.getCode())
            return ResponseEntity.ok(response);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
    }
}
