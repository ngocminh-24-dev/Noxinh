package org.company.noxinh.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.company.noxinh.constants.ResponseCode;
import org.company.noxinh.dto.ResponseData;
import org.company.noxinh.dto.banner.request.CreateBannerRequestDTO;
import org.company.noxinh.dto.banner.request.DisplayBannerRequestDTO;
import org.company.noxinh.dto.banner.request.UpdateBannerRequestDTO;
import org.company.noxinh.dto.banner.response.BannerResponseDTO;
import org.company.noxinh.dto.banner.response.BannerResponseDTOV2;
import org.company.noxinh.service.BannerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Banner controller.
 */
@RestController
@RequestMapping("/api/v1/banner")
@RequiredArgsConstructor
public class BannerController {
    private final BannerService bannerService;

    /**
     * Create banner response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ResponseData<String>> createBanner(@RequestBody @Valid CreateBannerRequestDTO request) {
        if (request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C005.getCode(), ResponseCode.C005.getMessage()));
        }
        ResponseData<String> addBanner = bannerService.createBanner(request);
        if (addBanner.getStatus() == ResponseCode.C001.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(addBanner);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(addBanner);
    }

    /**
     * Change display response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PutMapping("/display")
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ResponseData<String>> changeDisplay(@RequestBody @Valid DisplayBannerRequestDTO request) {
        if (request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C005.getCode(), ResponseCode.C005.getMessage()));
        }
        ResponseData<String> changeDisplay = bannerService.changeDisplay(request);
        if (changeDisplay.getStatus() == ResponseCode.C001.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(changeDisplay);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(changeDisplay);
    }

    /**
     * Gets banner display.
     *
     * @return the banner display
     */
    @GetMapping("/display")
    public ResponseEntity<ResponseData<List<BannerResponseDTO>>> getBannerDisplay() {
        ResponseData<List<BannerResponseDTO>> response = bannerService.getBannerDisplay();
        if (response.getStatus() == ResponseCode.C001.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Delete banner response entity.
     *
     * @param bannerID the banner id
     * @return the response entity
     */
    @DeleteMapping("/{bannerID}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ResponseData<String>> deleteBanner(@PathVariable("bannerID") Integer bannerID) {
        if (bannerID == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C005.getCode(), ResponseCode.C005.getMessage()));
        }
        ResponseData<String> response = bannerService.deleteBanner(bannerID);
        if (response.getStatus() == ResponseCode.C001.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Update banner response entity.
     *
     * @param bannerID the banner id
     * @param request  the request
     * @return the response entity
     */
    @PutMapping("/{bannerID}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ResponseData<String>> updateBanner(@PathVariable("bannerID") Integer bannerID, @RequestBody @Valid UpdateBannerRequestDTO request) {
        if (request == null || bannerID == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C005.getCode(), ResponseCode.C005.getMessage()));
        }
        ResponseData<String> response = bannerService.updateBanner(bannerID, request);
        if (response.getStatus() == ResponseCode.C001.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Get banner list response entity.
     *
     * @return the response entity
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ResponseData<Page<BannerResponseDTOV2>>> getBannerList(Pageable pageable){
        ResponseData<Page<BannerResponseDTOV2>> response = bannerService.getBannerList(pageable);
        if (response.getStatus() == ResponseCode.C001.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
