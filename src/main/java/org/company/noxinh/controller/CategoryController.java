package org.company.noxinh.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.company.noxinh.constants.ResponseCode;
import org.company.noxinh.dto.ResponseData;
import org.company.noxinh.dto.category.request.UpdateCategoryRequestDTO;
import org.company.noxinh.dto.category.response.CategoryResponseDTO;
import org.company.noxinh.dto.category.request.CreateCategoryRequestDTO;
import org.company.noxinh.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * The type Category controller.
 */
@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * Gets category.
     *
     * @param categoryId the category id
     * @return the category
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<ResponseData<CategoryResponseDTO>> getCategory(@PathVariable int categoryId) {
        ResponseData<CategoryResponseDTO> response = categoryService.getById(categoryId);
        if (response.getStatus() == ResponseCode.C001.getCode())
            return ResponseEntity.ok(response);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
    }

    /**
     * Delete category response entity.
     *
     * @param categoryId the category id
     * @return the response entity
     */
    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ResponseData<CategoryResponseDTO>> deleteCategory(@PathVariable int categoryId) {
        ResponseData<CategoryResponseDTO> response = categoryService.deleteById(categoryId);
        if (response.getStatus() == ResponseCode.C001.getCode())
            return ResponseEntity.ok(response);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
    }

    /**
     * Create category response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ResponseData<CategoryResponseDTO>> createCategory(@RequestBody @Valid CreateCategoryRequestDTO request) {
        if (request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C005.getCode(), ResponseCode.C005.getMessage()));
        }
        ResponseData<CategoryResponseDTO> resultOfCreate = categoryService.createCategory(request);
        if (resultOfCreate.getStatus() == ResponseCode.C001.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(resultOfCreate);
        }
        if (resultOfCreate.getStatus() == ResponseCode.C004.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(resultOfCreate);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultOfCreate);
    }

    /**
     * Gets categories.
     *
     * @param pageable the pageable
     * @return the categories
     */
    @GetMapping()
    public ResponseEntity<ResponseData<Page<CategoryResponseDTO>>> getCategories(Pageable pageable) {
        ResponseData<Page<CategoryResponseDTO>> resultOfList = categoryService.findAll(pageable);
        if (resultOfList.getStatus() == ResponseCode.C001.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(resultOfList);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultOfList);
    }

    /**
     * Update categories response entity.
     *
     * @param categoryId the category id
     * @param request    the request
     * @return the response entity
     */
    @PutMapping("/{categoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ResponseData<String>> updateCategories(@PathVariable("categoryId") Integer categoryId, @RequestBody @Valid UpdateCategoryRequestDTO request) {
        if (request == null || categoryId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C005.getCode(), ResponseCode.C005.getMessage()));
        }
        ResponseData<String> resultOfUpdate = categoryService.updateCategory(categoryId, request);
        if (resultOfUpdate.getStatus() == ResponseCode.C001.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(resultOfUpdate);
        } else if (resultOfUpdate.getStatus() == ResponseCode.C003.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultOfUpdate);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultOfUpdate);
    }

    /**
     * Attach product to category response entity.
     *
     * @param categoryId the category id
     * @param productId  the product id
     * @return the response entity
     */
    @PostMapping("/{categoryId}/attach-product/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ResponseData<String>> attachProductToCategory(@PathVariable("categoryId") Integer categoryId, @PathVariable("productId") Integer productId) {
        if (categoryId == null || productId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C005.getCode(), ResponseCode.C005.getMessage()));
        }
        ResponseData<String> resultOfCreate = categoryService.attachProductToCategory(categoryId, productId);
        if (resultOfCreate.getStatus() == ResponseCode.C001.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(resultOfCreate);
        } else if (resultOfCreate.getStatus() == ResponseCode.C004.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(resultOfCreate);
        } else if (resultOfCreate.getStatus() == ResponseCode.C003.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultOfCreate);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultOfCreate);
    }
}
