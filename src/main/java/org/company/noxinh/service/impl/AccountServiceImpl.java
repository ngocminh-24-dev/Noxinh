package org.company.noxinh.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.company.noxinh.constants.ResponseCode;
import org.company.noxinh.constants.ResponseMessage;
import org.company.noxinh.dto.ResponseData;
import org.company.noxinh.dto.authen.request.ChangePasswordRequestDTO;
import org.company.noxinh.dto.authen.request.UpdateAccountResponseDTO;
import org.company.noxinh.dto.authen.response.AccountProfileResponseDTO;
import org.company.noxinh.entity.Account;
import org.company.noxinh.entity.District;
import org.company.noxinh.entity.Province;
import org.company.noxinh.entity.Ward;
import org.company.noxinh.exception.exceptions.DataExistedException;
import org.company.noxinh.repository.AccountRepository;
import org.company.noxinh.repository.DistrictRepository;
import org.company.noxinh.repository.ProvinceRepository;
import org.company.noxinh.repository.WardRepository;
import org.company.noxinh.service.AccountService;
import org.company.noxinh.service.ValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

/**
 * The type User service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final WardRepository wardRepository;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final ValidationService validationService;

    @Override
    public ResponseData<AccountProfileResponseDTO> getAccount() {
        try {
            log.info("Start [getAccount] processing get account");
            Integer accountId = ((Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            Account account = accountRepository.findById(accountId).orElse(null);
            if (account == null) {
                return new ResponseData<>(ResponseCode.C003.getCode(), ResponseMessage.E013.getMessage());
            }
            Ward ward = wardRepository.findById(account.getWardId()).orElseThrow(() -> new RuntimeException(ResponseMessage.E005.getMessage()));
            District district = districtRepository.findById(account.getDistrictId()).orElseThrow(() -> new RuntimeException(ResponseMessage.E006.getMessage()));
            Province province = provinceRepository.findById(account.getProvinceId()).orElseThrow(() -> new RuntimeException(ResponseMessage.E007.getMessage()));

            AccountProfileResponseDTO accountProfileResponseDTO = modelMapper.map(account, AccountProfileResponseDTO.class);
            accountProfileResponseDTO.setWard(ward);
            accountProfileResponseDTO.setProvince(province);
            accountProfileResponseDTO.setDistrict(district);
            log.info("End [getAccount] processing get account");
            return new ResponseData<>(ResponseCode.C001.getCode(), ResponseMessage.S006.getMessage(), accountProfileResponseDTO);
        } catch (Exception ex) {
            log.error("Error getting account: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C007.getCode(), ResponseMessage.E008.getMessage());
        }
    }

    @Override
    public ResponseData<?> changePassword(ChangePasswordRequestDTO request, Principal principal) {
        try {
            log.info("Start [changePassword] processing change password of account");
            if (request == null) {
                return new ResponseData<>(ResponseCode.C005.getCode(), ResponseCode.C005.getMessage());
            }
            var account = (Account) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
            // Check if the current password is correct
            if (!passwordEncoder.matches(request.getCurrentPassword(), account.getPassword())) {
                return new ResponseData<>(ResponseCode.C002.getCode(), ResponseMessage.E009.getMessage());
            }
            // Check if the two password is the same
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                return new ResponseData<>(ResponseCode.C002.getCode(), ResponseMessage.E010.getMessage());
            }
            if (request.getNewPassword().equals(request.getCurrentPassword())) {
                return new ResponseData<>(ResponseCode.C002.getCode(), ResponseMessage.E011.getMessage());
            }
            account.setPassword(passwordEncoder.encode(request.getNewPassword()));
            accountRepository.save(account);
            log.info("End [changePassword] processing change password of account");
            return new ResponseData<>(ResponseCode.C001.getCode(), ResponseMessage.S007.getMessage());
        } catch (Exception ex) {
            log.error("changePassword error: {}", ex.getMessage());
        }
        return new ResponseData<>(ResponseCode.C007.getCode(), ResponseMessage.E012.getMessage());
    }

    @Override
    public ResponseData<UpdateAccountResponseDTO> updateAccount(UpdateAccountResponseDTO request) {
        try {
            log.info("Start [updateAccount] processing update account");
            Integer accountId = ((Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            if (request == null) {
                return new ResponseData<>(ResponseCode.C005.getCode(), ResponseCode.C005.getMessage());
            }
            Account account = accountRepository.findById(accountId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.E013.getMessage()));
            boolean isChanged = false;
            boolean isPhoneChange = ValidationService.updateIfChanged(request.getPhone(), account.getPhone(), account::setPhone);
            if (isPhoneChange) {
                validationService.validatePhoneNumber(request.getPhone());
            }
            ValidationService.updateIfChanged(request.getName(), account.getName(), account::setName);
            ValidationService.updateIfChanged(request.getSpecificAddress(), account.getSpecificAddress(), account::setSpecificAddress);
            ValidationService.updateIfChanged(request.getProvince_id(), account.getProvinceId(), account::setProvinceId);
            ValidationService.updateIfChanged(request.getWard_id(), account.getWardId(), account::setWardId);
            ValidationService.updateIfChanged(request.getDistrict_id(), account.getDistrictId(), account::setDistrictId);
            if (!isChanged) {
                accountRepository.save(account);
                log.info("End [updateAccount] processing update account");
                return new ResponseData<>(ResponseCode.C001.getCode(), ResponseMessage.S009.getMessage());
            }
        } catch (DataExistedException de) {
            return new ResponseData<>(ResponseCode.C004.getCode(), ResponseMessage.E015.getMessage());
        } catch (Exception ex) {
            log.error("Error update account: {}", ex.getMessage());
        }
        return new ResponseData<>(ResponseCode.C007.getCode(), ResponseMessage.E016.getMessage());
    }

    public Account findById(Integer id){
        return accountRepository.findById(id).orElse(null);
    }



    @Override
    public ResponseData<Integer> countAllOrders(){
        return ResponseData.ok(Integer.parseInt(String.valueOf(accountRepository.count())), "Đếm tổng người dùng thành công.");
    }
}
