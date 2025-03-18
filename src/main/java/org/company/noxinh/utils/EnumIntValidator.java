package org.company.noxinh.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.company.noxinh.utils.enum_validator.EnumInt;

public class EnumIntValidator implements ConstraintValidator<EnumInt, Integer> {
    private int min;
    private int max;

    @Override
    public void initialize(EnumInt constraintAnnotation) {
        this.min = constraintAnnotation.min(); // Set the min value from the annotation
        this.max = constraintAnnotation.max(); // Set the min value from the annotation
    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext context) {
        return integer != null && integer <= min  && integer >= max;
    }
}
