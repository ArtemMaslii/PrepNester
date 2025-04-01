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
}
