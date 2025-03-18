package org.company.noxinh.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.company.noxinh.constants.ResponseCode;
import org.company.noxinh.constants.ResponseMessage;
import org.company.noxinh.dto.ResponseData;
import org.company.noxinh.dto.admin.response.AccountResposnseDTO;
import org.company.noxinh.entity.Account;
import org.company.noxinh.repository.AccountRepository;
import org.company.noxinh.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * The type Admin service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseData<Page<AccountResposnseDTO>> getAccounts(String name, String phone, String specificAddress, Pageable pageable) {
        log.info("Start [getAccounts] processing get list accounts");
        Page<AccountResposnseDTO> accounts = accountRepository.findAccounts(name, phone, specificAddress, pageable);
        log.info("End [getAccounts] processing get list accounts");
        return new ResponseData<>(ResponseCode.C001.getCode(), ResponseMessage.S005.getMessage(), accounts);
    }

    @Override
    public ResponseData<String> changeDefaultPassword(Integer accountId) {
        try {
            log.info("Start [changeDefaultPassword] processing change password accountId: {}", accountId);
            Account account = accountRepository.findById(accountId)
                    .orElseThrow(null);
            if (account == null) {
                return new ResponseData<>(ResponseCode.C003.getCode(), ResponseMessage.E013.getMessage());
            }
            // Set password account default is 1
            account.setPassword(passwordEncoder.encode("1"));
            accountRepository.save(account);
        } catch (Exception ex) {
            log.error("Error while change password account: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C007.getCode(), ResponseMessage.E017.getMessage());
        }
        log.info("End [changeDefaultPassword] processing change password accountId: {}", accountId);
        return new ResponseData<>(ResponseCode.C001.getCode(), ResponseMessage.S010.getMessage());
    }
}
