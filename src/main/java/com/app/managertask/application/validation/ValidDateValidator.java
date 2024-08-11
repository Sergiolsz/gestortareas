package com.app.managertask.application.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class ValidDateValidator implements ConstraintValidator<ValidDate, LocalDateTime> {

  @Override
  public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
    if (value == null) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("Date cannot be null")
          .addConstraintViolation();
      return false;
    }

    if (!value.isAfter(LocalDateTime.now())) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("The date must be in the future.")
          .addConstraintViolation();
      return false;
    }

    return true;
  }
}