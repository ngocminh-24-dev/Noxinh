package org.company.noxinh.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.company.noxinh.constants.ResponseCode;
import org.company.noxinh.dto.ResponseData;
import org.company.noxinh.dto.combo.request.CreateComboRequest;
import org.company.noxinh.dto.combo.request.UpdateComboRequest;
import org.company.noxinh.dto.combo.request.UpdateComboStatusRequest;
import org.company.noxinh.dto.combo.response.ComboResponseDTO;
import org.company.noxinh.service.ComboService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Combo controller.
 */
@RestController
@RequestMapping("/api/v1/combo")
@RequiredArgsConstructor
public class ComboController {
    private final ComboService comboService;

    /**
     * Create combo response entity.
     *
     * @param createComboRequest the create combo request
     * @return the response entity
     */
    @PostMapping("/")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseData<String>> createCombo(CreateComboRequest createComboRequest) {
        ResponseData<String> response = comboService.createCombo(createComboRequest);
        return getResponse(response);
    }

    /**
     * Delete combo response entity.
     *
     * @param comboId the combo id
     * @return the response entity
     */
    @DeleteMapping("/{comboId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseData<String>> deleteCombo(@PathVariable Integer comboId) {
        ResponseData<String> response = comboService.deleteCombo(comboId);
        return getResponse(response);
    }

    /**
     * Update combo response entity.
     *
     * @param comboId            the combo id
     * @param updateComboRequest the update combo request
     * @return the response entity
     */
    @PutMapping("/{comboId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseData<String>> updateCombo(@PathVariable Integer comboId, UpdateComboRequest updateComboRequest) {
        ResponseData<String> response = comboService.updateCombo(updateComboRequest, comboId);
        return getResponse(response);
    }

    /**
     * Gets combo.
     *
     * @param comboId the combo id
     * @return the combo
     */
    @GetMapping("/{comboId}/")
    public ResponseEntity<ResponseData<ComboResponseDTO>> getCombo(@PathVariable Integer comboId,
                                                                   Pageable pageable,
                                                                   @RequestParam(required = false) Double minPrice,
                                                                   @RequestParam(required = false) Double maxPrice,
                                                                   @RequestParam(required = false) String search) {
        ResponseData<ComboResponseDTO> response = comboService.getComboResponseById(comboId, pageable, minPrice, maxPrice, search);
        return getResponse(response);
    }

    /**
     * Add product response entity.
     *
     * @param comboId   the combo id
     * @param productId the product id
     * @return the response entity
     */
    @PutMapping("/add/{comboId}/product/{productId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseData<String>> addProduct(@PathVariable(name = "comboId") Integer comboId, @PathVariable(name = "productId") Integer productId) {
        ResponseData<String> response = comboService.addProductToCombo(comboId, productId);
        return getResponse(response);
    }

    /**
     * Remove product response entity.
     *
     * @param comboId   the combo id
     * @param productId the product id
     * @return the response entity
     */
    @PutMapping("/remove/{comboId}/product/{productId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseData<String>> removeProduct(@PathVariable(name = "comboId") Integer comboId, @PathVariable(name = "productId") Integer productId) {
        ResponseData<String> response = comboService.removeProductFromCombo(comboId, productId);
        return getResponse(response);
    }

    @PutMapping("/status/{comboId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseData<String>> updateStatus(@PathVariable(name = "comboId") Integer comboId, @RequestBody UpdateComboStatusRequest updateComboStatusRequest) {
        ResponseData<String> response = comboService.adminUpdateComboStatus(comboId, updateComboStatusRequest.getComboStatus());
        return getResponse(response);
    }

    @GetMapping("/")
    public ResponseEntity<ResponseData<Page<ComboResponseDTO>>> getCombos(
            @Qualifier("comboPage") Pageable comboPage,
            @Qualifier("productPage") Pageable productPage) {
        ResponseData<Page<ComboResponseDTO>> response = comboService.getCombo(comboPage, productPage);
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
}
