package org.company.noxinh.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.company.noxinh.constants.ResponseCode;
import org.company.noxinh.dto.ResponseData;
import org.company.noxinh.dto.combo.response.ComboResponseDTO;
import org.company.noxinh.service.ComboService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/slider")
@RequiredArgsConstructor
public class SliderController {
    private final ComboService comboService;

    @GetMapping("/")
    public ResponseEntity<ResponseData<ComboResponseDTO>> getSlider(Pageable pageable) {
        ResponseData<ComboResponseDTO> response = comboService.getComboResponseById(10, pageable);
        return getResponse(response);
    }


    /**
     * Remove product response entity.
     *
     * @param productId the product id
     * @return the response entity
     */
    @PutMapping("/remove-product/{productId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseData<String>> removeProduct(@PathVariable(name = "productId") Integer productId) {
        ResponseData<String> response = comboService.removeProductFromCombo(10, productId);
        return getResponse(response);
    }

    /**
     * Add product response entity.
     *
     * @param productId the product id
     * @return the response entity
     */
    @PutMapping("/add-product/{productId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseData<String>> addProduct(@PathVariable(name = "productId") Integer productId) {
        ResponseData<String> response = comboService.addProductToCombo(10, productId);
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
