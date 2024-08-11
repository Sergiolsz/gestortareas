package com.app.managertask.application.validation;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ValidDateValidatorTest {

  private ValidDateValidator validator;

  private ConstraintValidatorContext context;

  @BeforeEach
  void setUp() {
    validator = new ValidDateValidator();
    context = Mockito.spy(ConstraintValidatorContext.class);
  }

  @Test
  void testIsValid_NullDate() {
    ConstraintViolationBuilder builder = mock(ConstraintViolationBuilder.class);
    when(context.buildConstraintViolationWithTemplate("Date cannot be null")).thenReturn(builder);
    when(builder.addConstraintViolation()).thenReturn(context);

    boolean isValid = validator.isValid(null, context);

    assertFalse(isValid);
    verify(context).disableDefaultConstraintViolation();
    verify(context).buildConstraintViolationWithTemplate("Date cannot be null");
    verify(builder).addConstraintViolation();
  }

  @Test
  void testIsValid_PastDate() {
    ConstraintViolationBuilder builder = mock(ConstraintViolationBuilder.class);
    when(context.buildConstraintViolationWithTemplate("The date must be in the future.")).thenReturn(builder);
    when(builder.addConstraintViolation()).thenReturn(context);

    boolean isValid = validator.isValid(LocalDateTime.now().minusDays(1), context);

    assertFalse(isValid);
    verify(context).disableDefaultConstraintViolation();
    verify(context).buildConstraintViolationWithTemplate("The date must be in the future.");
    verify(builder).addConstraintViolation();
  }

  @Test
  void testIsValid_FutureDate() {
    boolean isValid = validator.isValid(LocalDateTime.now().plusDays(1), context);

    assertTrue(isValid);
    verify(context, never()).disableDefaultConstraintViolation();
    verify(context, never()).buildConstraintViolationWithTemplate(anyString());
  }
}