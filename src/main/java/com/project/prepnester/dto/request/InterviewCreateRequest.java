package com.project.prepnester.dto.request;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class InterviewCreateRequest {

  private String candidateFullName;

  private String email;

  private String phoneNumber;

  private String openPosition;

  private String department;

  private String notes;

  private UUID createdBy;
}
