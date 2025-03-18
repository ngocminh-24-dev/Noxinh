package org.company.noxinh.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.company.noxinh.exception.exceptions.DataExistedException;
import org.company.noxinh.repository.AccountRepository;
import org.company.noxinh.service.ValidationService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * The type Validation service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ValidationServiceImpl implements ValidationService {
    private final AccountRepository accountRepository;

    @Override
    public void validateRegister(String phone, String email) throws DataExistedException {
        Map<String, String> errors = new HashMap<>();

        validateAndPutError(errors, "phone", () -> validatePhone(phone));
        validateAndPutError(errors, "email", () -> validateEmail(email));
        if (!errors.isEmpty())
            throw new DataExistedException("Dữ liệu đã tồn tại!", errors);
    }

    @Override
    public void validatePhoneNumber(String phone) throws DataExistedException {
        log.info("Checking phone number availability.");
        boolean phoneAlreadyExists = Stream.of(
                accountRepository.findFirstByPhone(phone)
        ).anyMatch(Optional::isPresent);
        if (phoneAlreadyExists) {
            log.error("Phone number: {} is already taken!", phone);
            throw new DataExistedException("Số điện thoại đã được sử dụng");
        }
    }

    /**
     * Validate and put error.
     *
     * @param errors     the errors
     * @param field      the field
     * @param validation the validation
     */
    public static void validateAndPutError(Map<String, String> errors, String field, Runnable validation) {
        try {
            validation.run();
        } catch (DataExistedException e) {
            errors.put(field, e.getMessage());
        }
    }

    /**
     * Validate phone boolean.
     *
     * @param phone the phone
     * @throws DataExistedException the data existed exception
     */
    public void validatePhone(String phone) throws DataExistedException {
        log.info("Checking phone availability.");
        String errorMessage = "Số điện thoại đã được sử dụng";
        if (accountRepository.findFirstByPhone(phone).isPresent()) {
            log.error("Phone: {} not available!", phone);
            throw new DataExistedException(errorMessage);
        }
    }

    /**
     * Validate email.
     *
     * @param email the email
     * @throws DataExistedException the data existed exception
     */
    public void validateEmail(String email) throws DataExistedException {
        log.info("Checking email availability.");
        String errorMessage = "Email đã được sử dụng";
        if (accountRepository.findFirstByEmail(email).isPresent()) {
            log.error("Email: {} not available!", email);
            throw new DataExistedException(errorMessage);
        }
    }
}
