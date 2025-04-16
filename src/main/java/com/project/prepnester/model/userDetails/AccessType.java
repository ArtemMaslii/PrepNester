package com.project.prepnester.model.userDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AccessType {
  GUEST("GUEST"),
  CANDIDATE("CANDIDATE"),
  ADMIN("ADMIN");

  private final String value;
}
