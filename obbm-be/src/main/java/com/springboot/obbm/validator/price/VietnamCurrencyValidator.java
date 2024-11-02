package com.springboot.obbm.validator.price;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class VietnamCurrencyValidator implements ConstraintValidator<VietnamCurrency, Double> {

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return true;
        }
        return value > 0 && value % 1000 == 0;
    }
}
