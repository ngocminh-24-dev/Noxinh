package org.company.noxinh.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.company.noxinh.config.LogoutConfig;
import org.company.noxinh.constants.ResponseCode;
import org.company.noxinh.dto.ResponseData;
import org.company.noxinh.dto.authen.request.ChangePasswordRequestDTO;
import org.company.noxinh.dto.authen.request.UpdateAccountResponseDTO;
import org.company.noxinh.dto.authen.response.AccountProfileResponseDTO;
import org.company.noxinh.dto.authen.response.LoginResponseDTO;
import org.company.noxinh.service.AccountService;
import org.company.noxinh.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

/**
 * The type Account controller.
 */
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
@RestController
@SecurityRequirement(name = "Bearer")
public class AccountController {
    private final LogoutConfig logoutConfig;
    private final AccountService accountService;
    private final AuthenticationService authenticationService;

    /**
     * Logout.
     *
     * @param request        the request
     * @param response       the response
     * @param authentication the authentication
     */
    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        logoutConfig.logout(request, response, authentication);
    }

    /**
     * Gets account.
     *
     * @return the account
     */
    @GetMapping("/profile")
    public ResponseEntity<ResponseData<AccountProfileResponseDTO>> getAccount() {
        ResponseData<AccountProfileResponseDTO> user = accountService.getAccount();
        if (user.getStatus() == ResponseCode.C001.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else if (user.getStatus() == ResponseCode.C003.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(user);
    }

    /**
     * Change password response entity.
     *
     * @param changePasswordRequestDTO the change password request dto
     * @param principal                the principal
     * @return the response entity
     */
    @PutMapping("/change-password")
    public ResponseEntity<ResponseData<?>> changePassword(@RequestBody @Valid ChangePasswordRequestDTO changePasswordRequestDTO, Principal principal) {
        if (changePasswordRequestDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C005.getCode(), ResponseCode.C005.getMessage()));
        }
        ResponseData<?> changePasswordAccount = accountService.changePassword(changePasswordRequestDTO, principal);
        if (changePasswordAccount.getStatus() == ResponseCode.C001.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(changePasswordAccount);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(changePasswordAccount);
    }

    /**
     * Update user response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PutMapping("/profile")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ResponseData<UpdateAccountResponseDTO>> updateUser(@RequestBody @Valid UpdateAccountResponseDTO request) {
        if (request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C005.getCode(), ResponseCode.C005.getMessage()));
        }
        ResponseData<UpdateAccountResponseDTO> user = accountService.updateAccount(request);
        if (user.getStatus() == ResponseCode.C001.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else if (user.getStatus() == ResponseCode.C003.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
        } else if (user.getStatus() == ResponseCode.C004.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(user);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(user);
    }

    /**
     * Refresh token.
     *
     * @param request  the request
     * @param response the response
     * @return the response entity
     * @throws IOException the io exception
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<ResponseData<LoginResponseDTO>> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResponseData<LoginResponseDTO> result = authenticationService.refreshToken(request, response);
        if (result.getStatus() == ResponseCode.C001.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }



    @GetMapping("/count-all")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseData<Integer>> countAll() {
        ResponseData<Integer> response = accountService.countAllOrders();
        if (response.getStatus() == ResponseCode.C001.getCode())
            return ResponseEntity.ok(response);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
    }
}
