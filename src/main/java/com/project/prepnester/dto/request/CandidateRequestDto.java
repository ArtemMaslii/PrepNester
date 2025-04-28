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
public class CandidateRequestDto {

  private String fullName;

  private String email;

  private String phoneNumber;

  private String rawPassword;

  private UUID createdBy;
}
