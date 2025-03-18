package org.company.noxinh.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.company.noxinh.utils.enum_validator.EnumPrice;

public class EnumPriceValidator implements ConstraintValidator<EnumPrice, Double> {
    @Override
    public boolean isValid(Double price, ConstraintValidatorContext constraintValidatorContext) {
        return price >= 0;
    }
}
