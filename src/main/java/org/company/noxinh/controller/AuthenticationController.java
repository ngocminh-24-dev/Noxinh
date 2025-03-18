package org.company.noxinh.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.company.noxinh.constants.ResponseCode;
import org.company.noxinh.dto.ResponseData;
import org.company.noxinh.dto.authen.request.*;
import org.company.noxinh.dto.authen.response.LoginResponseDTO;
import org.company.noxinh.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The type Authentication controller.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    /**
     * Login login response dto.
     *
     * @param request the request
     * @return the login response dto
     */
    @PostMapping("/login")
    public ResponseEntity<ResponseData<LoginResponseDTO>> login(@RequestBody @Valid LoginRequestDTO request) {
        if (request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C005.getCode(), ResponseCode.C005.getMessage()));
        }
        ResponseData<LoginResponseDTO> loginAccount = authenticationService.loginAccount(request);
        if (loginAccount.getStatus() == ResponseCode.C001.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(loginAccount);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(loginAccount);
    }

    /**
     * Register response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping("/register")
    public ResponseEntity<ResponseData<String>> register(@RequestBody @Valid RegisterRequestDTO request) {
        if (request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C005.getCode(), ResponseCode.C005.getMessage()));
        }
        ResponseData<String> registerAccount = authenticationService.register(request);
        if (registerAccount.getStatus() == ResponseCode.C001.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(registerAccount);
        } else if (registerAccount.getStatus() == ResponseCode.C004.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(registerAccount);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(registerAccount);
    }

    /**
     * Login admin response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping("/login-admin")
    public ResponseEntity<ResponseData<LoginResponseDTO>> loginAdmin(@RequestBody @Valid LoginAdminRequestDTO request) {
        if (request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C005.getCode(), ResponseCode.C005.getMessage()));
        }
        ResponseData<LoginResponseDTO> loginAccount = authenticationService.loginAdmin(request);
        if (loginAccount.getStatus() == ResponseCode.C001.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(loginAccount);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(loginAccount);
    }

    /**
     * Request reset password response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping("/reset-password")
    public ResponseEntity<ResponseData<String>> requestResetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        if (request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C005.getCode(), ResponseCode.C005.getMessage()));
        }
        ResponseData<String> result = authenticationService.resetPassword(request);
        if (result.getStatus() == ResponseCode.C001.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else if (result.getStatus() == ResponseCode.C003.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * Verify reset password response entity.
     *
     * @param uuid    the uuid
     * @param request the request
     * @return the response entity
     */
    @PostMapping("/verify-password/{UUID}")
    public ResponseEntity<ResponseData<String>> verifyResetPassword(@PathVariable("UUID") String uuid, @RequestBody @Valid ConfirmPasswordRequestDTO request) {
        if (uuid == null || request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C005.getCode(), ResponseCode.C005.getMessage()));
        }
        ResponseData<String> result = authenticationService.verifyResetPassword(uuid, request);
        if (result.getStatus() == ResponseCode.C001.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
}
