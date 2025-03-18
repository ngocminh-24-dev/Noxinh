package org.company.noxinh.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.company.noxinh.constants.ResponseCode;
import org.company.noxinh.dto.ResponseData;
import org.company.noxinh.dto.admin.response.AccountResposnseDTO;
import org.company.noxinh.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The type Admin controller.
 */
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer")
public class AdminController {
    private final AdminService adminService;

    /**
     * Get accounts response entity.
     *
     * @param name            the name
     * @param phone           the phone
     * @param specificAddress the specific address
     * @param pageable        the pageable
     * @return the response entity
     */
    @GetMapping("/account")
    public ResponseEntity<ResponseData<Page<AccountResposnseDTO>>> getAccounts(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "phone", required = false) String phone,
            @RequestParam(name = "specificAddress", required = false) String specificAddress,
            Pageable pageable) {
        ResponseData<Page<AccountResposnseDTO>> result = adminService.getAccounts(name, phone, specificAddress, pageable);
        if (result.getStatus() == ResponseCode.C001.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Change default password response entity.
     *
     * @param accountId the account id
     * @return the response entity
     */
    @PostMapping("/accounts/password/set-default/{accountId}")
    public ResponseEntity<ResponseData<String>> changeDefaultPassword(@PathVariable("accountId") Integer accountId) {
        if (accountId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C005.getCode(), ResponseCode.C005.getMessage()));
        }
        ResponseData<String> resultOfChange = adminService.changeDefaultPassword(accountId);
        if (resultOfChange.getStatus() == ResponseCode.C001.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(resultOfChange);
        }
        if (resultOfChange.getStatus() == ResponseCode.C003.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultOfChange);
        }
        return new ResponseEntity<>(resultOfChange, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
