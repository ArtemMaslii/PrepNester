package com.project.prepnester.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AccessType {
  READ_WRITE("READ_WRITE"),
  READ("READ");

  private final String accessType;
}
