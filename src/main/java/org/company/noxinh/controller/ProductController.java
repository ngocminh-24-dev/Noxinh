package org.company.noxinh.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.company.noxinh.constants.ProductStatus;
import org.company.noxinh.constants.ResponseCode;
import org.company.noxinh.dto.ResponseData;
import org.company.noxinh.dto.product.ProductResponseDTO;
import org.company.noxinh.dto.product.request.CreateProductRequest;
import org.company.noxinh.dto.product.request.UpdateProductRequest;
import org.company.noxinh.dto.product.request.UpdateProductStatusRequest;
import org.company.noxinh.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * The type Product controller.
 */
@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    /**
     * Gets product.
     *
     * @param pageable        the pageable
     * @param includeCategory the include category
     * @return the product
     */
    @GetMapping("/include-categories/{includeCategory}")
    public ResponseEntity<ResponseData<Page<ProductResponseDTO>>> getProduct(Pageable pageable,
                                                                             @PathVariable Boolean includeCategory,
                                                                             @RequestParam(required = false) Integer comboId,
                                                                             @RequestParam(required = false) ProductStatus productStatus) {
        ResponseData<Page<ProductResponseDTO>> response = productService.getProduct(pageable, includeCategory, comboId, productStatus);
        return getResponse(response);
    }

    /**
     * Gets product.
     *
     * @param productId       the product id
     * @param includeCategory the include category
     * @return the product
     */
    @GetMapping("/{productId}/include-categories/{include-category}")
    public ResponseEntity<ResponseData<ProductResponseDTO>> getProduct(@PathVariable("productId") int productId, @PathVariable("include-category") Boolean includeCategory) {
        ResponseData<ProductResponseDTO> response = productService.getProductById(productId, includeCategory);
        return getResponse(response);
    }

    /**
     * Delete product response entity.
     *
     * @param productId the product id
     * @return the response entity
     */
    @DeleteMapping("/{productId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseData<ProductResponseDTO>> deleteProduct(@PathVariable("productId") int productId) {
        ResponseData<ProductResponseDTO> response = productService.deleteProductById(productId);
        return getResponse(response);
    }

    /**
     * Update product response entity.
     *
     * @param productId the product id
     * @param request   the request
     * @return the response entity
     */
    @PutMapping("/info/{productId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseData<ProductResponseDTO>> updateProduct(@PathVariable("productId") int productId, @RequestBody UpdateProductRequest request) {
        ResponseData<ProductResponseDTO> response = productService.updateProduct(productId, request);
        return getResponse(response);
    }

    /**
     * Update product status response entity.
     *
     * @param productId the product id
     * @param status    the status
     * @return the response entity
     */
    @PutMapping("/status/{productId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseData<ProductResponseDTO>> updateProductStatus(@PathVariable("productId") int productId, @RequestBody UpdateProductStatusRequest status) {
        ResponseData<ProductResponseDTO> response = productService.updateProductStatus(productId, status.getProductStatus());
        return getResponse(response);
    }

    /**
     * Create product response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping("/")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseData<ProductResponseDTO>> createProduct(@RequestBody CreateProductRequest request) {
        ResponseData<ProductResponseDTO> response = productService.createProduct(request);
        return getResponse(response);
    }

    /**
     * Gets response.
     *
     * @param <T>      the type parameter
     * @param response the response
     * @return the response
     */
    public <T> ResponseEntity<ResponseData<T>> getResponse(ResponseData<T> response) {
        if (response.getStatus() == ResponseCode.C001.getCode())
            return ResponseEntity.ok(response);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
    }

    /**
     * Gets product by name.
     *
     * @param productName the product name
     * @param pageable    the pageable
     * @return the product by name
     */
    @GetMapping("/search")
    public ResponseEntity<ResponseData<Page<ProductResponseDTO>>> getProductByName(@RequestParam(value = "productName", required = false) String productName, Pageable pageable) {
        ResponseData<Page<ProductResponseDTO>> response = productService.getProductByName(productName, pageable);
        return getResponse(response);
    }



    @GetMapping("/count-all")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseData<Integer>> countAll() {
        return getResponse(productService.countAllOrders());
    }
}
