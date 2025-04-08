package com.project.prepnester.validation;

import com.project.prepnester.model.Gender;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GenderValidator.class)
public @interface GenderConstraint {

  String message() default "Invalid gender value. Must be one of: FEMALE, MALE, PREFER_NOT_TO_SAY";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}

class GenderValidator implements ConstraintValidator<GenderConstraint, Gender> {

  @Override
  public boolean isValid(Gender gender, ConstraintValidatorContext context) {
    return gender != null;
  }
}