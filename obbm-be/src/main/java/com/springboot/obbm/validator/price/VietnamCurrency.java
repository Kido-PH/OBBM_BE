package com.springboot.obbm.validator.price;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Target({ FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { VietnamCurrencyValidator.class })
public @interface VietnamCurrency {
    String message() default "Định dạng tiền tệ không hợp lệ, phải là số dương theo bội số của 1.000";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
