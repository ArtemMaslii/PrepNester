package com.project.prepnester.model.userDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AccessType {
  READ_WRITE("READ_WRITE"),
  READ("READ");

  private final String value;
}
