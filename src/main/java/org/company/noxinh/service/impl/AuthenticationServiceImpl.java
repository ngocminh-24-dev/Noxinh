package org.company.noxinh.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.company.noxinh.constants.ResponseCode;
import org.company.noxinh.constants.ResponseMessage;
import org.company.noxinh.constants.Role;
import org.company.noxinh.constants.TokenType;
import org.company.noxinh.dto.ResponseData;
import org.company.noxinh.dto.authen.request.*;
import org.company.noxinh.dto.authen.response.LoginResponseDTO;
import org.company.noxinh.entity.Account;
import org.company.noxinh.entity.AccountToken;
import org.company.noxinh.exception.exceptions.DataExistedException;
import org.company.noxinh.repository.AccountRepository;
import org.company.noxinh.repository.AccountTokenRepository;
import org.company.noxinh.service.AuthenticationService;
import org.company.noxinh.service.JwtService;
import org.company.noxinh.service.OTPService;
import org.company.noxinh.service.ValidationService;
import org.company.noxinh.utils.EmailUtils;
import org.company.noxinh.utils.OTPUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * The type Authentication service.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final AccountTokenRepository accountTokenRepository;
    private final ValidationService validationService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final OTPUtils otpUtils;
    private final OTPService otpService;
    private final EmailUtils emailUtils;


    @Override
    public ResponseData<LoginResponseDTO> loginAccount(LoginRequestDTO request) {
        log.info("Start processing function login account");
        String accessToken;
        String refreshToken;
        try {
            log.info("Start [loginAccount] processing login with user has phone: {}", request.getPhone());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getPhone(), request.getPassword(), List.of(new SimpleGrantedAuthority(Role.USER.toString()))));
            var account = accountRepository.findByPhone(request.getPhone())
                    .orElseThrow(() -> new RuntimeException(ResponseMessage.E001.getMessage()));
            accessToken = jwtService.generateToken(account);
            refreshToken = jwtService.generateRefreshToken(account);
            saveToken(account, refreshToken);
        } catch (Exception ex) {
            log.error("Error occurred while login: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C002.getCode(), ResponseMessage.E002.getMessage());
        }
        log.info("End [loginAccount] processing function login account");
        return new ResponseData<>(ResponseCode.C001.getCode(), ResponseMessage.S001.getMessage(), LoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());
    }

    @Override
    public ResponseData<String> register(RegisterRequestDTO request) {
        try {
            log.info("Start [register] processing function register account");
            validationService.validateRegister(request.getPhone(), request.getEmail());
            Account account = modelMapper.map(request, Account.class);
            account.setPassword(passwordEncoder.encode(request.getPassword()));
            accountRepository.save(account);
            log.info("End [register] processing function register account");
            return new ResponseData<>(ResponseCode.C001.getCode(), ResponseMessage.S008.getMessage());
        } catch (DataExistedException de) {
            String errorMessage = de.getErrors().get("email") != null ? de.getErrors().get("email") : de.getErrors().get("phone");
            return new ResponseData<>(ResponseCode.C004.getCode(), errorMessage);
        } catch (Exception ex) {
            log.error("Error occurred while register account: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C002.getCode(), ResponseMessage.E014.getMessage());
        }
    }

    @Override
    public ResponseData<LoginResponseDTO> loginAdmin(LoginAdminRequestDTO request) {
        log.info("Start processing function login admin");
        String accessToken;
        String refreshToken;
        try {
            log.info("Start [loginAdmin] processing login with role admin");
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getPhone(), request.getPassword(), List.of(new SimpleGrantedAuthority(Role.ADMIN.toString()))));
            var account = accountRepository.findByPhone(request.getPhone())
                    .orElseThrow(() -> new RuntimeException(ResponseMessage.E001.getMessage()));
            accessToken = jwtService.generateToken(account);
            refreshToken = jwtService.generateRefreshToken(account);
            saveToken(account, refreshToken);
        } catch (Exception ex) {
            log.error("Error occurred while login with role admin: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C002.getCode(), ResponseMessage.E002.getMessage());
        }
        log.info("End [loginAdmin] processing login with role admin");
        return new ResponseData<>(ResponseCode.C001.getCode(), ResponseMessage.S001.getMessage(), LoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());
    }

    @Override
    public ResponseData<String> resetPassword(ResetPasswordRequest request) {
        try {
            if (request == null) {
                return new ResponseData<>(ResponseCode.C005.getCode(), ResponseCode.C005.getMessage());
            }
            log.info("Start [resetPassword] processing to reset password account");
            String email = request.getEmail();
            if (otpService.isRateLimitExceeded(email)) {
                log.warn("Reset password request rate limit exceeded for email: {}", email);
                return new ResponseData<>(ResponseCode.C210.getCode(), ResponseMessage.E032.getMessage());
            }
            otpService.incrementResetRequestCount(email);
            Account account = findByEmail(request.getEmail());
            if (account == null) {
                log.error("User with email: {} not found", request.getEmail());
                return new ResponseData<>(ResponseCode.C210.getCode(), ResponseMessage.E033.getMessage());
            }
            UUID uuid = UUID.randomUUID();
            String otp = otpUtils.generateOTP();
            otpService.saveAccountRedis(uuid, otp, account, LocalDateTime.now());
            if (!emailUtils.sendOtpEmail(request.getEmail(), otp)) {
                throw new IllegalArgumentException("Can not send otp to email");
            }
            return new ResponseData<>(ResponseCode.C001.getCode(), ResponseMessage.S021.getMessage(), uuid.toString());
        } catch (RuntimeException e) {
            log.error("Can not send otp to email: {}", e.getMessage());
        } finally {
            log.info("End [resetPassword] processing to reset password account");
        }
        return new ResponseData<>(ResponseCode.C002.getCode(), ResponseMessage.E034.getMessage());
    }

    @Override
    public ResponseData<String> verifyResetPassword(String uuid, ConfirmPasswordRequestDTO request) {
        if (uuid == null || request == null) {
            return new ResponseData<>(ResponseCode.C005.getCode(), ResponseCode.C005.getMessage());
        }
        try {
            log.info("Start [verifyResetPassword] processing verify rest password account");
            OTPRedisCache accountRedis = otpService.getAccountRedis(uuid);
            if (accountRedis == null) {
                return new ResponseData<>(ResponseCode.C007.getCode(), ResponseMessage.E035.getMessage());
            }
            if (!accountRedis.getUuid().toString().equals(uuid)) {
                return new ResponseData<>(ResponseCode.C007.getCode(), ResponseMessage.E037.getMessage());
            } else if (!accountRedis.getOtp().equals(request.getOtp())) {
                return new ResponseData<>(ResponseCode.C007.getCode(), ResponseMessage.E036.getMessage());
            }
            if (Duration.between(accountRedis.getOtpGeneratedTime(), LocalDateTime.now()).getSeconds() < 10 * 60) {
                // Check if the two password is the same
                if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                    return new ResponseData<>(ResponseCode.C002.getCode(), ResponseMessage.E010.getMessage());
                }
                Account account = accountRepository.findById(accountRedis.getAccount().getId()).orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng"));
                account.setPassword(passwordEncoder.encode(request.getNewPassword()));
                accountRepository.save(account);
                otpService.deleteAccountRedis(uuid);
                otpService.deleteResetRequestCount(account.getEmail());
                return new ResponseData<>(ResponseCode.C001.getCode(), ResponseMessage.S022.getMessage());
            } else {
                return new ResponseData<>(ResponseCode.C007.getCode(), ResponseMessage.E038.getMessage());
            }
        } catch (Exception ex) {
            log.error("Error while verify password: {}", ex.getMessage());
        } finally {
            log.info("End [verifyResetPassword] processing verify reset password account");
        }
        return new ResponseData<>(ResponseCode.C007.getCode(), ResponseMessage.E039.getMessage());
    }

    private Account findByEmail(String email) {
        return accountRepository.findByEmail(email).orElse(null);
    }

    private void saveToken(Account account, String refreshToken) {
        var token = AccountToken.builder()
                .accountId(account.getId())
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .refreshToken(refreshToken)
                .build();
        accountTokenRepository.save(token);
    }

    @Override
    public ResponseData<LoginResponseDTO> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            final String oldRefreshToken;
            final String userName;
            log.info("Start [refreshToken] processing refresh token account");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return null;
            }
            oldRefreshToken = authHeader.substring(7);
            userName = jwtService.extractUsername(oldRefreshToken);
            if (userName != null) {
                var user = this.accountRepository.findByPhone(userName)
                        .orElseThrow();
                if (jwtService.isTokenValid(oldRefreshToken, user)) {
                    var accessToken = jwtService.generateToken(user);
                    var newRefreshToken = jwtService.generateRefreshToken(user);
                    // find old token and update it
                    saveRefreshToken(user, oldRefreshToken, newRefreshToken);
                    var authResponse = LoginResponseDTO.builder()
                            .accessToken(accessToken)
                            .refreshToken(newRefreshToken)
                            .build();
                    return new ResponseData<>(ResponseCode.C001.getCode(), ResponseMessage.S023.getMessage(), authResponse);
                }
            }
        } catch (Exception e) {
            log.error("Error while refresh token: {}", e.getMessage());
        } finally {
            log.info("End [refreshToken] processing refresh token account");
        }
        return new ResponseData<>(ResponseCode.C007.getCode(), ResponseMessage.E040.getMessage());
    }

    private void saveRefreshToken(Account account, String oldRefreshToken, String newRefreshToken) {
        AccountToken token = accountTokenRepository.findByRefreshToken(oldRefreshToken).orElse(null);
        if (token != null) {
            token.setAccountId(account.getId());
            token.setRefreshToken(newRefreshToken);
            token.setTokenType(TokenType.BEARER);
            token.setExpired(false);
            token.setRevoked(false);
            accountTokenRepository.save(token);
        } else {
            // Handle the case when the token is not found, e.g., throw an exception or create a new token.
            throw new IllegalArgumentException("Token not found for the provided refresh token");
        }
    }
}
