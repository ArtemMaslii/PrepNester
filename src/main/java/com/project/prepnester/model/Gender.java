package com.project.prepnester.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Gender {
  FEMALE("F"),
  MALE("M"),
  PREFER_NOT_TO_SAY("X");

  private final String value;

  public static Gender fromValue(String value) {
    for (Gender gender : Gender.values()) {
      if (gender.value.equalsIgnoreCase(value)) {
        return gender;
      }
    }
    throw new IllegalArgumentException("Invalid gender value: " + value);
  }
}
