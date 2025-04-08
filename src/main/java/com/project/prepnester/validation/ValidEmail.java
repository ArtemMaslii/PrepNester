package com.project.prepnester.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
public @interface ValidEmail {

  String message() default "Invalid email format, please check if it matches such pattern: example@host.com";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}

class EmailValidator implements ConstraintValidator<ValidEmail, String> {

  private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,}$";

  @Override
  public boolean isValid(String email, ConstraintValidatorContext context) {
    if (email == null || email.isEmpty()) {
      return false;
    }
    return Pattern.matches(EMAIL_REGEX, email);
  }
}