package com.project.prepnester.model.interview;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
  IN_PROGRESS("IN_PROGRESS"),
  COMPLETE("COMPLETE"),
  CANCELLED("CANCELLED");

  private final String value;

  public static Status fromValue(String value) {
    for (Status status : Status.values()) {
      if (status.value.equalsIgnoreCase(value)) {
        return status;
      }
    }
    throw new IllegalArgumentException("Invalid status value: " + value);
  }
}
