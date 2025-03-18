package org.company.noxinh.controller;

import lombok.RequiredArgsConstructor;
import org.company.noxinh.constants.ResponseCode;
import org.company.noxinh.dto.ResponseData;
import org.company.noxinh.dto.address.response.DistrictResponseDTO;
import org.company.noxinh.dto.address.response.ProvinceResponseDTO;
import org.company.noxinh.dto.address.response.WardResponseDTO;
import org.company.noxinh.service.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Address controller.
 */
@RestController
@RequestMapping("/api/v1/address/")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    /**
     * Gets district.
     *
     * @param provinceId the province id
     * @return the district
     */
    @GetMapping("/district/{provinceId}")
    public ResponseEntity<ResponseData<List<DistrictResponseDTO>>> getDistrict(@PathVariable(name = "provinceId") Integer provinceId) {
        if (provinceId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C005.getCode(), ResponseCode.C005.getMessage()));
        }
        ResponseData<List<DistrictResponseDTO>> result = addressService.findDistrict(provinceId);
        if (result.getStatus() == ResponseCode.C001.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * Gets province.
     *
     * @return the province
     */
    @GetMapping("/province")
    public ResponseEntity<ResponseData<List<ProvinceResponseDTO>>> getProvince() {
        ResponseData<List<ProvinceResponseDTO>> result = addressService.findProvince();
        if (result.getStatus() == ResponseCode.C001.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * Gets ward.
     *
     * @param districtId the district id
     * @return the ward
     */
    @GetMapping("/ward/{districtId}")
    public ResponseEntity<ResponseData<List<WardResponseDTO>>> getWard(@PathVariable(name = "districtId") Integer districtId) {
        if (districtId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C005.getCode(), ResponseCode.C005.getMessage()));
        }
        ResponseData<List<WardResponseDTO>> result = addressService.findWard(districtId);
        if (result.getStatus() == ResponseCode.C001.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
}
