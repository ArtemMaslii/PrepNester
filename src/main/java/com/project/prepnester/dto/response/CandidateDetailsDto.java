package com.project.prepnester.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CandidateDetailsDto {

  private String fullName;

  private String email;

  private String phoneNumber;

  private String rawPassword;
}
