package com.project.prepnester.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
    BindingResult result = ex.getBindingResult();
    StringBuilder errorMessage = new StringBuilder("Validation errors: ");
    for (FieldError fieldError : result.getFieldErrors()) {
      errorMessage.append(fieldError.getField())
          .append(" - ")
          .append(fieldError.getDefaultMessage())
          .append("; ");
    }
    return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
  }
}