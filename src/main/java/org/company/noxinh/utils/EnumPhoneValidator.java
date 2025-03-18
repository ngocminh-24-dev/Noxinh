package org.company.noxinh.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.company.noxinh.utils.enum_validator.EnumPhone;

/**
 * The type Enum phone validator.
 */
public class EnumPhoneValidator implements ConstraintValidator<EnumPhone, String> {
    @Override
    public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
        return phone.matches("^0\\d{9,10}$");
    }
}
