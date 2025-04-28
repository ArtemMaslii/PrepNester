package com.project.prepnester.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class CandidateUpdateRequestDto {

  private String fullName;

  private String email;

  private String phoneNumber;
}
