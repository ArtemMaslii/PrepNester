package com.project.prepnester.util.exceptions;

public class UserDetailsNotFoundException extends RuntimeException {

  public UserDetailsNotFoundException(String message) {
    super(message);
  }

  public UserDetailsNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
